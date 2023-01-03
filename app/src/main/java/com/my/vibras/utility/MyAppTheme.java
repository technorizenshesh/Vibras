package com.my.vibras.utility;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.my.vibras.R;

import org.jetbrains.annotations.NotNull;

interface MyAppTheme  {
    int firstActivityBackgroundColor(@NotNull Context context);
    int firstActivityTextColor(@NotNull Context context);
    int firstActivityIconColor(@NotNull Context context);
    // any other methods for other elements

    public class LightTheme implements MyAppTheme {
        public int id() { // set unique iD for each theme
            return 0;
        }

        public int firstActivityBackgroundColor(@NotNull Context context) {
            return ContextCompat.getColor(context, R.color.white);
        }

        public int firstActivityTextColor(@NotNull Context context) {
            return ContextCompat.getColor(context,  R.color.black);
        }

        public int firstActivityIconColor(@NotNull Context context) {
            return ContextCompat.getColor(context, R.color.white);
        }


    }

    public class NightTheme implements MyAppTheme {
        public int id() { // set unique iD for each theme
        return 0;
    }

        public int firstActivityBackgroundColor(@NotNull Context context) {
            return ContextCompat.getColor(context, R.color.black);
        }

        public int firstActivityTextColor(@NotNull Context context) {
            return ContextCompat.getColor(context,  R.color.white);
        }

        public int firstActivityIconColor(@NotNull Context context) {
            return ContextCompat.getColor(context, R.color.black);
        }

    }
}