package com.ynr.keypsd.mobileprogrammingsemesterproject.Activities;

import static com.ynr.keypsd.mobileprogrammingsemesterproject.Adapters.MemoryAdapter.MEMORY_OBJECT;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestReadExternalStoragePermission;
import static com.ynr.keypsd.mobileprogrammingsemesterproject.Utils.RequestPermission.checkAndRequestWriteExternalStoragePermission;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Enums.EnumSelectedMode;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.SqliteDatabaseHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.DateHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.ImageHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers.PdfHelper;
import com.ynr.keypsd.mobileprogrammingsemesterproject.Models.Memory;
import com.ynr.keypsd.mobileprogrammingsemesterproject.R;

public class AddOrUpdateMemoryActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int REQUEST_TAKE_FROM_GALLERY = 102;

    private Button datePickerButton;
    private ImageView[] emojiIcons;
    private EditText titleEt;
    private EditText memoryContentEt;
    private Button selectImageOrVideoButton;
    private ImageView memoryImageIV;
    private VideoView memoryVideoView;
    private TextInputEditText memoryPasswordEt;
    private LinearLayout memoryLayout;
    private TextInputLayout memoryPasswordLayout;

    private DateHelper dateHelper;
    private SqliteDatabaseHelper databaseHelper;
    private PdfHelper pdfHelper;
    private EnumSelectedMode selectedMode;
    private Uri selectedUri;
    private int currentMemoryId;

    private enum EnumAddOrUpdate{
        ADD,
        UPDATE
    }
    EnumAddOrUpdate addOrUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_memory);

        define();
        dateHelper = new DateHelper(AddOrUpdateMemoryActivity.this, datePickerButton);
        databaseHelper = new SqliteDatabaseHelper(AddOrUpdateMemoryActivity.this);
        setEmojiIcons();

        Bundle bundle = getIntent().getExtras();
        addOrUpdate = (bundle != null && bundle.getSerializable(MEMORY_OBJECT) != null)
                        ? EnumAddOrUpdate.UPDATE
                        : EnumAddOrUpdate.ADD;

        if(addOrUpdate == EnumAddOrUpdate.UPDATE) { // update
            Memory memory = (Memory) bundle.getSerializable(MEMORY_OBJECT);
            currentMemoryId = memory.getId();
            dateHelper.initializeDatePickerForUpdate(memory.getDate());
            ImageHelper.applyGrayscaleFilterToAllImageViews(emojiIcons);
            emojiIcons[memory.getMode()].clearColorFilter();
            titleEt.setText(memory.getTitle());
            memoryContentEt.setText(memory.getMainText());

            if(!memory.getMediaUri().equals("")){
                Uri uri = Uri.parse(memory.getMediaUri());
                ContentResolver cr = getContentResolver();
                String type = cr.getType(uri);
                if(type.startsWith("image")){ //image
                    memoryImageIV.setVisibility(View.VISIBLE);
                    memoryImageIV.setImageURI(uri);
                }
                else { // video
                    memoryVideoView.setVisibility(View.VISIBLE);
                    memoryVideoView.setVideoURI(uri);
                    MediaController mc = new MediaController(this);
                    memoryVideoView.setMediaController(mc);
                }
            }

            memoryPasswordEt.setText(memory.getPassword());

        }
        else{ // add
            dateHelper.initializeDatePickerForAdd();
        }


    }

    private void setEmojiIcons() {
        // set default emoji
        selectedMode = EnumSelectedMode.HAPPY;
        ImageHelper.applyGrayscaleFilterToAllImageViews(emojiIcons);
        emojiIcons[0].clearColorFilter();

        for(ImageView imageView : emojiIcons){
            imageView.setOnClickListener(view -> {
                ImageHelper.applyGrayscaleFilterToAllImageViews(emojiIcons);
                imageView.clearColorFilter();
                selectedMode = getModeFromEmoji(imageView.getId());
            });
        }
    }

    private EnumSelectedMode getModeFromEmoji(int id){

        switch (id){
            case R.id.happyEmojiIcon: return EnumSelectedMode.HAPPY;
            case R.id.sadEmojiIcon:   return EnumSelectedMode.SAD;
            case R.id.angryEmojiIcon: return EnumSelectedMode.ANGRY;
            default: throw new IllegalArgumentException("Illegal argument for view id: " + id);
        }
    }

    private int getModeAsInteger(EnumSelectedMode selectedMode){
        switch (selectedMode){
            case HAPPY: return 0;
            case SAD:   return 1;
            case ANGRY: return 2;
            default:    throw new IllegalArgumentException("Invalid mode : " + selectedMode.name());
        }
    }

    private void define() {
        datePickerButton = findViewById(R.id.datePickerButton);
        emojiIcons = new ImageView[]{findViewById(R.id.happyEmojiIcon),
                                     findViewById(R.id.sadEmojiIcon),
                                     findViewById(R.id.angryEmojiIcon)};

        selectImageOrVideoButton = findViewById(R.id.selectImageOrVideoButton);
        memoryImageIV = findViewById(R.id.memoryImageIV);
        memoryVideoView = findViewById(R.id.memoryVideoView);
        memoryPasswordEt = findViewById(R.id.memoryPasswordEt);
        titleEt = findViewById(R.id.titleEt);
        memoryContentEt = findViewById(R.id.memoryContentEt);
        memoryLayout = findViewById(R.id.memory_layout);
        memoryPasswordLayout = findViewById(R.id.memoryPasswordLayout);

        datePickerButton.setOnClickListener(this);
        selectImageOrVideoButton.setOnClickListener(this);

        selectedUri = Uri.parse("");
        pdfHelper = new PdfHelper(AddOrUpdateMemoryActivity.this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.datePickerButton){
            dateHelper.openDatePicker();
        }
        else if(view.getId() == R.id.selectImageOrVideoButton){
            if(checkAndRequestReadExternalStoragePermission(AddOrUpdateMemoryActivity.this)){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* video/*");
                startActivityForResult(intent , REQUEST_TAKE_FROM_GALLERY);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_or_update_memory, menu);

        MenuItem okButton = menu.findItem(R.id.add_or_update_ok_button);
        okButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                Memory memory = new Memory(
                        dateHelper.getCurrentSelectedDate(),
                        getModeAsInteger(selectedMode),
                        titleEt.getText().toString(),
                        memoryContentEt.getText().toString(),
                        selectedUri.toString(),
                        memoryPasswordEt.getText().toString());

                if(addOrUpdate == EnumAddOrUpdate.UPDATE){
                    memory.setId(currentMemoryId);
                    databaseHelper.updateMemory(memory);
                    Toast.makeText(AddOrUpdateMemoryActivity.this, "Anı güncellendi!", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(AddOrUpdateMemoryActivity.this, "Anı eklendi!", Toast.LENGTH_SHORT).show();
                    databaseHelper.insertMemory(memory);
                }

                Intent intent = new Intent(AddOrUpdateMemoryActivity.this, MemoryManagementActivity.class);
                startActivity(intent);
                return false;
            }
        });

        MenuItem convertToPdfButton = menu.findItem(R.id.convert_to_pdf_button);
        if(addOrUpdate == EnumAddOrUpdate.ADD)
            convertToPdfButton.setVisible(false);

        convertToPdfButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(!checkAndRequestWriteExternalStoragePermission(AddOrUpdateMemoryActivity.this))
                    return false;

                selectImageOrVideoButton.setVisibility(View.GONE);
                memoryPasswordLayout.setVisibility(View.GONE);

                Bitmap bitmap = PdfHelper.loadBitmap(memoryLayout, memoryLayout.getWidth(), memoryLayout.getHeight());
                pdfHelper.createAndOpenPdf(bitmap, titleEt.getText().toString());

                selectImageOrVideoButton.setVisibility(View.VISIBLE);
                memoryPasswordLayout.setVisibility(View.VISIBLE);

                return true;
            }
        });


        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_FROM_GALLERY:  // Get from gallery result
                if (data != null) {
                    try {
                        if(data.getData()!=null){
                            Uri uri = data.getData();
                            selectedUri = uri;
                            ContentResolver cr = getContentResolver();
                            String type = cr.getType(uri);

                            if(type.startsWith("image")){ //image
                                memoryImageIV.setVisibility(View.VISIBLE);
                                memoryVideoView.setVisibility(View.GONE);
                                memoryImageIV.setImageURI(uri);
                            }
                            else { // video
                                memoryVideoView.setVisibility(View.VISIBLE);
                                memoryImageIV.setVisibility(View.GONE);
                                memoryVideoView.setVideoURI(uri);
                                MediaController mc = new MediaController(this);
                                memoryVideoView.setMediaController(mc);
                                memoryVideoView.start();
                            }
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }

                }
                break;
        }
    }






}