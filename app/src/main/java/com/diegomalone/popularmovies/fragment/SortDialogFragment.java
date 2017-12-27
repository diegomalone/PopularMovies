package com.diegomalone.popularmovies.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;

import com.diegomalone.popularmovies.R;
import com.diegomalone.popularmovies.utils.SortUtils;

/**
 * Created by Diego Malone on 26/12/17.
 */

public class SortDialogFragment extends DialogFragment {

    public static final String BROADCAST_SORT_PREFERENCES_CHANGED = "sortPreferencesChanged";
    public static final String TAG = SortDialogFragment.class.getSimpleName();

    private SortUtils sortUtils;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        sortUtils = new SortUtils(PreferenceManager.getDefaultSharedPreferences(getActivity()));

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.sort_dialog_title)
                .setNegativeButton(R.string.cancel_button, null)
                .setSingleChoiceItems(R.array.preference_list_type_entries,
                        sortUtils.getSortPreferenceOrdinal(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sortUtils.saveSortPreference(sortUtils.getPreferenceFromOrdinal(i));
                                sendPreferencesChangedBroadcast();
                                dialogInterface.dismiss();
                            }
                        });

        return alertDialog.create();
    }

    private void sendPreferencesChangedBroadcast() {
        Intent intent = new Intent(BROADCAST_SORT_PREFERENCES_CHANGED);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
}
