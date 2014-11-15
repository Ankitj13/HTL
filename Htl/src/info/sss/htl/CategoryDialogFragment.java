package info.sss.htl;

import info.androidhive.htl.R;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class CategoryDialogFragment extends DialogFragment {

	public interface NoticeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog ,int value);
        public void onDialogNegativeClick(DialogFragment dialog ,int value);
	}

	private ArrayList mSelectedItems;
	 // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
	private int cateid2;
    
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    // Set the dialog title
	    builder.setTitle(R.string.pick_category)
	    // Specify the list array, the items to be selected by default (null for none),
	    // and the listener through which to receive callbacks when items are selected
	           .setSingleChoiceItems(R.array.categories, cateid2, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int cateid) {
					// TODO Auto-generated method stub
					 mListener.onDialogPositiveClick(CategoryDialogFragment.this, cateid);
					 dismiss();
				}
			})
	    // Set the action buttons
	         /*  .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	                   // User clicked OK, so save the mSelectedItems results somewhere
	                   // or return them to the component that opened the dialog
	                 
	               }
	           })*/
	           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {
	            	   dismiss();
	               }
	           });

	    return builder.create();
	}
}
