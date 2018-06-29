package com.example.infinum.learningandroid.ui.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.infinum.learningandroid.R;
import com.example.infinum.learningandroid.model.Pokemon;
import com.example.infinum.learningandroid.model.interactors.StorageInteractorImpl;
import com.example.infinum.learningandroid.model.interactors.contracts.StorageInteractor;
import com.example.infinum.learningandroid.utils.ResourceConverter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Optional;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by infinum on 18/07/2017.
 */

public class AddFragment extends Fragment {

    public interface AddListener {
        void onPokemonAdded(Pokemon pokemon);
        void onInputChanged();
    }

    private static final int GALLERY_PERMISSION_REQUEST = 1;
    private static final int CAMERA_PERMISSION_REQUEST = 2;
    private static final int PICK_IMAGE = 1;
    private static final int IMAGE_CAPTURE = 2;

    private static final String HAS_IMAGE = "HAS_IMAGE";
    private static final String IMAGE = "IMAGE";

    @BindView(R.id.addImage)
    ImageView addImage;

    @BindView(R.id.addNameInput)
    EditText nameInput;

    @BindView(R.id.addHeightInput)
    EditText heightInput;

    @BindView(R.id.addWeightInput)
    EditText weightInput;

    @BindView(R.id.addCategoryInput)
    EditText categoryInput;

    @BindView(R.id.addAbilitiesInput)
    EditText abilitiesInput;

    @BindView(R.id.addDescriptionInput)
    EditText descriptionInput;

    @BindView(R.id.addProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.addGenderInput)
    EditText genderInput;

    private Unbinder unbinder;
    private boolean hasImage;
    private List<AddListener> listeners;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listeners = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            hasImage = savedInstanceState.getBoolean(HAS_IMAGE);
            if(hasImage) {
                setImage((Bitmap)savedInstanceState.getParcelable(IMAGE));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(HAS_IMAGE, hasImage);
        if(hasImage) {
            outState.putParcelable(IMAGE, ((BitmapDrawable)addImage.getDrawable()).getBitmap());
        }
    }

    @Optional
    @OnClick(R.id.addSaveButton)
    protected void onSaveClicked() {
        savePokemon();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.addSaveButton_menu:
                savePokemon();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @OnClick(R.id.addFloatingAddButton)
    protected void onAddImageButtonClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choose_dialog_title));
        builder.setMessage(getString(R.string.choose_dialog_message));
        builder.setPositiveButton(getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            GALLERY_PERMISSION_REQUEST);
                }
                else {
                    openGallery();
                }
            }
        });

        builder.setNegativeButton(getString(R.string.camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if(permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                            CAMERA_PERMISSION_REQUEST);
                }
                else {
                    openCamera();
                }
            }
        });
        builder.show();
    }

    @OnTextChanged({R.id.addNameInput, R.id.addHeightInput, R.id.addWeightInput, R.id.addCategoryInput,
            R.id.addAbilitiesInput, R.id.addDescriptionInput})
    protected void onTextChanged() {
        for(AddListener listener : listeners) {
            listener.onInputChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.d(String.valueOf(grantResults[0]) + " : " + String.valueOf(PackageManager.PERMISSION_DENIED));
        switch(requestCode) {
            case GALLERY_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.d("granted!");
                    openGallery();
                }
                break;
            case CAMERA_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE:
                if(resultCode == RESULT_OK) {
                    if(data != null) {
                        try {
                            InputStream inputStream = getActivity().getContentResolver()
                                    .openInputStream(data.getData());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            setImage(bitmap);
                        }
                        catch (FileNotFoundException e) {
                            Toast.makeText(getActivity(), getString(R.string.error_file_not_found),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getActivity(), getString(R.string.error_load_gallery_image),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case IMAGE_CAPTURE:
                if(resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    setImage(bitmap);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void setImage(Bitmap bitmap) {
        addImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addImage.setImageBitmap(bitmap);
        hasImage = true;
    }

    private void openGallery() {
        Timber.d("openGallery()");
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if(galleryIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(galleryIntent, getString(R.string.open_gallery_with)),
                    PICK_IMAGE);
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.error_no_gallery_app), Toast.LENGTH_SHORT).show();
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(cameraIntent, getString(R.string.open_camera_with)),
                    IMAGE_CAPTURE);
        }
        else {
            Toast.makeText(getActivity(), getString(R.string.error_no_camera_app), Toast.LENGTH_SHORT).show();
        }
    }

    public void savePokemon() {
        if(!checkInputs()) {
            return;
        }

        final Pokemon pokemon = new Pokemon();
        pokemon.setName(nameInput.getText().toString());
        pokemon.setHeight(heightInput.getText().toString());
        pokemon.setWeight(weightInput.getText().toString());
        pokemon.setCategory(categoryInput.getText().toString());
        pokemon.setAbilities(abilitiesInput.getText().toString());
        pokemon.setDescription(descriptionInput.getText().toString());
        pokemon.setGender(genderInput.getText().toString());
        if(hasImage) {
            final Bitmap bitmap = ((BitmapDrawable)addImage.getDrawable()).getBitmap();
            StorageInteractorImpl interactor = new StorageInteractorImpl();
            interactor.saveImage(ResourceConverter.imageToByteArray(bitmap),
                    new StorageInteractor.StorageListener() {
                        @Override
                        public void onSuccess(String imagePath) {
                            pokemon.setImagePath(imagePath);
                            for(AddListener listener : listeners) {
                                listener.onPokemonAdded(pokemon);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Timber.d(error);
                            for(AddListener listener : listeners) {
                                listener.onPokemonAdded(pokemon);
                            }
                        }
                    });
        }
        else {
            for(AddListener listener : listeners) {
                listener.onPokemonAdded(pokemon);
            }
        }
    }

    private boolean checkInputs() {
        String name = nameInput.getText().toString();
        String height = heightInput.getText().toString();
        String weight = weightInput.getText().toString();
        String category = categoryInput.getText().toString();
        String abilities = abilitiesInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String gender = genderInput.getText().toString();
        if(name.isEmpty()) {
            nameInput.setError(getString(R.string.error_requires_name));
            return false;
        }
        else if(height.isEmpty() || Double.parseDouble(height) == 0) {
            heightInput.setError(getString(R.string.error_requires_height));
            return false;
        }
        else if(weight.isEmpty() || Double.parseDouble(weight) == 0) {
            weightInput.setError(getString(R.string.error_requires_weight));
            return false;
        }
        else if(category.isEmpty()) {
            categoryInput.setError(getString(R.string.error_requires_category));
            return false;
        }
        else if(abilities.isEmpty()) {
            abilitiesInput.setError(getString(R.string.error_requires_abilities));
            return false;
        }
        else if(description.isEmpty()) {
            descriptionInput.setError(getString(R.string.error_requires_description));
            return false;
        }
        else if(gender.isEmpty()) {
            genderInput.setError(getString(R.string.error_requires_gender));
            return false;
        }

        return true;
    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);

        nameInput.setFocusable(false);
        heightInput.setFocusable(false);
        weightInput.setFocusable(false);
        categoryInput.setFocusable(false);
        abilitiesInput.setFocusable(false);
        descriptionInput.setFocusable(false);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);

        nameInput.setFocusableInTouchMode(true);
        heightInput.setFocusableInTouchMode(true);
        weightInput.setFocusableInTouchMode(true);
        categoryInput.setFocusableInTouchMode(true);
        abilitiesInput.setFocusableInTouchMode(true);
        descriptionInput.setFocusableInTouchMode(true);
    }

    public void addAddListener(AddListener listener) {
        listeners.add(listener);
    }
}
