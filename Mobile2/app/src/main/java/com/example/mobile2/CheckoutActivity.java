package com.example.mobile2;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        EditText nameInput = findViewById(R.id.nameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText phoneInput = findViewById(R.id.phoneInput);

        View.OnFocusChangeListener scrollToFocus = (v, hasFocus) -> {
            if (hasFocus) {
                triggerScroll(v);
            }
        };

        if (nameInput != null) {
            nameInput.setOnFocusChangeListener(scrollToFocus);
        }
        if (emailInput != null) {
            emailInput.setOnFocusChangeListener(scrollToFocus);
        }
        if (phoneInput != null) {
            phoneInput.setOnFocusChangeListener(scrollToFocus);
        }


        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                private int previousHeight = 0;

                @Override
                public void onGlobalLayout() {
                    int currentHeight = rootView.getHeight();
                    if (previousHeight != 0 && currentHeight < previousHeight) {
                        View focused = getCurrentFocus();
                        if (focused instanceof EditText) {
                            triggerScroll(focused);
                        }
                    }
                    previousHeight = currentHeight;
                }
            });
        }
    }

    private void triggerScroll(View v) {
        if (v == null) return;

        Runnable doScroll = () -> {
            ScrollView scrollView = findViewById(R.id.scrollView);
            if (scrollView != null) {
                int top = getDeepChildTop(scrollView, v);
                int targetY = Math.max(0, top - 24);
                scrollView.smoothScrollTo(0, targetY);
            }
            v.requestRectangleOnScreen(new Rect(0, 0, v.getWidth(), v.getHeight()), false);
        };

        v.post(doScroll);
        v.postDelayed(doScroll, 150);
        v.postDelayed(doScroll, 350);
    }

    private int getDeepChildTop(ScrollView scrollView, View view) {
        int cumulativeTop = 0;
        View current = view;
        while (current != null && current != scrollView) {
            cumulativeTop += current.getTop();
            if (current.getParent() instanceof View) {
                current = (View) current.getParent();
            } else {
                break;
            }
        }
        return cumulativeTop;
    }
}
