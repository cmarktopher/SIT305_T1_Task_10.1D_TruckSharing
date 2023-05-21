package com.application.trucksharing.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
    private Uri selectedImageUri;

    // Keep track of if all inputs (including image) have been filled and password matches.
    Boolean canCreateAccount = false;

    public SignUpFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        handleFragmentTransitions();

        createOpenGalleryIntent();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create our binding and view
        binding = FragmentSignUpBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Bind to adding image
        binding.signUpFragmentImageCard.setOnClickListener(this::handleImageViewPressed);

        // Bind to create account button
        binding.signUpFragmentCreateAccountButton.setOnClickListener(this::handleCreateAccount);

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
                        binding.signUpFragmentImageView.setImageURI(selectedImageUri);
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
     * Response on image view pressed.
     * This will handle adding of an image.
     * As mentioned previously, we don't need to ask permissions anymore to open the gallery - the storage access framework is more secure.
     * As such, simply launch the activity (to my understanding, this will just be the system's picker)
     * @param view View pressed.
     */
    private void handleImageViewPressed(View view){

        galleryActivityResultLauncher.launch(galleryIntent);
    }


    /**
     * Response to create account button being pressed.
     * Handles creation of a new account.
     * @param view View pressed.
     */
    private void handleCreateAccount(View view){

        String fullName;
        String userName;
        String passWord;
        String confirmPassWord;
        String number;

        // Change this from using an exception to a boolean - this way, all fields will show an error if empty
        fullName = validateInputString(binding.signUpFragmentFullNameInputViewLayout, binding.signUpFragmentFullNameInputView, "A name must be provided");
        userName = validateInputString(binding.signUpFragmentUserNameInputLayout, binding.signUpFragmentUserNameInputView, "A username must be provided");
        passWord = validateInputString(binding.signUpFragmentPasswordInputLayout, binding.signUpFragmentPasswordInputView, "A password must be provided");
        confirmPassWord = validateInputString(binding.signUpFragmentConfirmPasswordInputLayout, binding.signUpFragmentConfirmPasswordInputView, "Please re-enter password for validation");
        number = validateInputString(binding.signUpFragmentPhoneInputLayout, binding.signUpFragmentPhoneInputView, "A phone number must be provided");

        // Need some extra logic to check if password matches
        if (!passWord.matches(confirmPassWord)) {

            canCreateAccount = false;
            binding.signUpFragmentPasswordInputLayout.setError("Passwords do not match");
            binding.signUpFragmentConfirmPasswordInputLayout.setError("Passwords do not match");
        }

        // Return if image is not set
        // For simplicity, I'll do a toast to tell the user to set an image
        if (selectedImageUri == null){

            canCreateAccount = false;
            Toast noImageToast = new Toast(getContext());
            noImageToast.setText("Please set a profile image");
            noImageToast.show();
        }

        // New logic here where we now check if we have everything required to create an account
        if (!canCreateAccount){

            return;
        }

        // Create a new user
        User newUser = new User(fullName, userName, passWord, number, selectedImageUri.toString());

        // Update the database with our new user
        UserViewModel userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        userViewModel.insertNewUser(newUser);

        // Return to sign up page
        Navigation.findNavController(getView()).popBackStack();
    }

    /**
     * This will check if we have actual input in the fields.
     * Also, I have introduced a new variable which helps keep track of whether we can create an account or now.
     */
    private String validateInputString(TextInputLayout textInputLayout, TextInputEditText textInputEditText, String errorMessage){

        canCreateAccount = true;

        // Clear the error to ensure subsequent entries are taken into account.
        textInputLayout.setError(null);

        // Get the text
        String textInput = textInputEditText.getText().toString();

        // If empty, set the error and set can create account to false
        if (textInput.isEmpty()){

            canCreateAccount = false;
            textInputLayout.setError(errorMessage);
        }

        return textInput;
    }
}