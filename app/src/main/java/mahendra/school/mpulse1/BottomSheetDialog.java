package mahendra.school.mpulse1;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    View v;
   Context contexter;

    BottomSheetDialog(Context context){
        contexter = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.custom_layout_sign_in,
                container, false);
        //v.findViewById(R.id.textView82).setVisibility(View.GONE);
        return v;
    }

}
