package com.application.trucksharing.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.application.trucksharing.DataModels.User;
import com.application.trucksharing.R;
import com.application.trucksharing.ViewModels.UserViewModel;
import com.application.trucksharing.databinding.FragmentSignUpBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialFadeThrough;

/**
 * Fragment for the sign up form
 * I'll provide a description for what I've done for the gallery selection and permissions:
 * Basically, I had issues trying to get the permissions dialogue to appear.
 * The same code and variants were used for audio permissions which worked fine but for some reason, the gallery dialogue would never appear.
 * Turns out, from Android 11 onwards, they have enforced a different system for file permissions through scoped storage which I believe is the reason why nothing could make the dialogue appear.
 * Now, at this point, I don't fully understand how to implement that system but what I did find through some research was something called the storage access framework
 * Here, to my understanding, we have the concept of a provider and the user can provide permissions to a file through the system's content picker UI.
 * By using this system, we don't need a dialogue to appear asking for permissions which to my understanding, would also grant unrestricted access to the files.
 * Now, the app won't have any access to the files directly and we use the content picker to provide the relevant files to the app at the user's discretion.
 * So while this deviates from the task requirements, traditional permissions doesn't work for the target sdk and this new method is more secure.
 * Sources:
 * https://developer.android.com/training/data-storage/shared/documents-files
 * https://developer.android.com/guide/topics/providers/document-provider
 */
public class SignUpFragment extends Fragment {

    // Binding to our UI elements
    private FragmentSignUpBinding binding;

    // Reference to our activity result launcher and intent to allow for going into gallery
    private Intent galleryIntent;
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher;

    // Cache to the image URI to be stored in the database
    Uri selectedImageUri;

    public SignUpFragment() {

    }

    public static SignUpFragment newInstance() {

        return new SignUpFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        createOpenGalleryIntent();

        handleFragmentTransitions();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to adding image
        binding.signUpImageCard.setOnClickListener(view1 -> handleOpenGallery());

        // Bind to create account button
        binding.createAccountButton.setOnClickListener(view1 -> handleCreateAccount(binding));

        // Inflate the layout for this fragment
        return view;
    }


    /**
     * Logic to create the needed requirements to open a gallery and set the selected image.
     * This needs to be called on an initialization callback such as onCreate - had an error if not done this way
     */
    private void createOpenGalleryIntent(){

        // Create a new intent, now using the storage access framework
        galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

        // Just a note about this
        // Here, we can set the type of file we want to be able to choose as mentioned in the description at the top of this code
        //So now, I am basically saying, choose png files for the image folder (we can technically use * to gain access to everything in the image folder)
        galleryIntent.setType("image/png");

        // Register the callback for activity result
        galleryActivityResultLauncher = registerForActivityResult(

                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK && result.getData() != null){

                        selectedImageUri = result.getData().getData();
                        binding.signUpImageView.setImageURI(selectedImageUri);
                    }
                }
        );
    }

    /**
     * Just a repeat method for handling transitions
     */
    private void handleFragmentTransitions(){

        MaterialFadeThrough enterFadeThrough = new MaterialFadeThrough();
        enterFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_enter_duration));

        MaterialFadeThrough exitFadeThrough = new MaterialFadeThrough();
        exitFadeThrough.setDuration(getResources().getInteger(R.integer.fade_through_exit_duration));

        setEnterTransition(enterFadeThrough);
        setExitTransition(exitFadeThrough);
    }

    /**
     * Handles adding of an image
     * As mentioned previously, we don't need to ask permissions anymore to open the gallery - the storage access framework is more secure.
     * As such, simply launch the activity (to my understanding, this will just be the system's picker)
     */
    private void handleOpenGallery(){

        galleryActivityResultLauncher.launch(galleryIntent);
    }


    /**
     * Handles creation of a new account.
     */
    private void handleCreateAccount(FragmentSignUpBinding binding){

        String fullName;
        String userName;
        String passWord;
        String confirmPassWord;
        String number;

        // Trying something different here
        // We will check if the user has provided the respective input, if not, an exception will be throw and we return out of this method and not add the new user.
        try {

            fullName = validateInputString(binding.signUpFullNameInputViewLayout, binding.signUpFullNameInputView, "A name must be provided");
            userName = validateInputString(binding.signUpUserNameInputLayout, binding.signUpUserNameInputView, "A username must be provided");
            passWord = validateInputString(binding.signUpPasswordInputLayout, binding.signUpPasswordInputView, "A password must be provided");
            confirmPassWord = validateInputString(binding.signUpConfirmPasswordInputLayout, binding.signUpConfirmPasswordInputView, "Please re-enter password for validation");
            number = validateInputString(binding.signUpPhoneInputLayout, binding.signUpPhoneInputView, "A phone number must be provided");

            // Can be handled better but if we reach here with no issues, we just need to ensure number has been cleared of any errors that may have been set.
            // Also, if passwords do not match, if that has been fixed by the user, and all entries are present, all errors will be gone by this point.
            binding.signUpPhoneInputLayout.setError(null);

        } catch (Exception e) {

            return;
        }

        // Need some extra logic to check if password matches
        if (!passWord.matches(confirmPassWord)) {

            binding.signUpPasswordInputLayout.setError("Passwords do not match");
            binding.signUpConfirmPasswordInputLayout.setError("Passwords do not match");
            return;
        }

        // Return if image is not set
        // For simplicity, I'll do a toast to tell the user to set an image
        if (selectedImageUri == null){

            Toast noImageToast = new Toast(getContext());
            noImageToast.setText("Please set a profile image");
            noImageToast.show();
            return;
        }

        // Create a new user
        User newUser = new User(fullName, userName, passWord, number, selectedImageUri.toString());

        // Update the database with our new user
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.insertNewUser(newUser);

        // Go back to home screen when done
        FragmentManager fragmentManager = ((AppCompatActivity) requireContext()).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.coreFragmentContainer, LogInFragment.newInstance(), null)
                .commit();
    }

    /**
     * Just a quick way to ensure we have inputs present in the fields.
     */
    private String validateInputString(TextInputLayout textInputLayout, TextInputEditText textInputEditText, String errorMessage){

        // Clear the error to ensure subsequent entries are taken into account.
        textInputLayout.setError(null);

        // Get the text
        String textInput = textInputEditText.getText().toString();

        // If empty, set the error and throw an exception
        if (textInput.isEmpty()){

            textInputLayout.setError(errorMessage);
            throw new RuntimeException();
        }

        // If we reach here, we have an input to use
        return textInput;
    }
}