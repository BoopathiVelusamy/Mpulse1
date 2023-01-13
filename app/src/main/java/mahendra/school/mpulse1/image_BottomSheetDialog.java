package mahendra.school.mpulse1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class image_BottomSheetDialog extends BottomSheetDialogFragment {

    View v;
    String image_arr;
    Context ctx;

    image_BottomSheetDialog(Context context, String images){

       image_arr = images;

       ctx = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.bottom_sheet_layout2,
                container, false);

        ImageView imageView = (ImageView)v.findViewById(R.id.imageView14);

        v.findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        try {
            Picasso.with(ctx)
                    .load(image_arr)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageView);
        }catch (Exception e){

        }


        return v;
    }

}
