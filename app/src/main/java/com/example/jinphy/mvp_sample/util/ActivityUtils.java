package com.example.jinphy.mvp_sample.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jinphy on 2017/7/30.
 */

public class ActivityUtils {


    public static void addFragmentToActivity(
            @NonNull FragmentManager fragmentManager,
            @NonNull Fragment fragment,
            int fragmentId
            ){
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragmentId, fragment);
        transaction.commit();
    }
}
