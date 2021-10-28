package com.laioffer.matrix;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
//set up view
//两个view：dialog page和report page
public class ReportDialog extends Dialog {
    private RecyclerView mRecyclerView;
    private ReportRecyclerViewAdapter mRecyclerViewAdapter;
    private ViewSwitcher mViewSwitcher;
    private String mEventType;
    //Event specs
    private ImageView mImageCamera;
    private Button mBackButton;
    private Button mSendButton;
    private EditText mCommentEditText;
    private ImageView mEventTypeImg;
    private TextView mTypeTextView;
    private DialogCallBack mDialogCallBack;//往外面forward

    interface DialogCallBack {
        void onSubmit(String editString, String event_type);
        void startCamera();
    }

    public void setDialogCallBack(DialogCallBack dialogCallBack) {
        mDialogCallBack = dialogCallBack;
    }

    public ReportDialog(@NonNull Context context) {
        this(context, R.style.MyAlertDialogStyle);
    }

    public ReportDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View dialogView = View.inflate(getContext(), R.layout.dialog, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setupRecyclerView(dialogView);
        mViewSwitcher = dialogView.findViewById(R.id.viewSwitcher);
        setUpEventSpecs(dialogView);
    }

    private void setupRecyclerView(View dialogView) {
        mRecyclerView = dialogView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerViewAdapter = new ReportRecyclerViewAdapter(getContext(), Config.listItems);
        mRecyclerViewAdapter.setClickListener(new ReportRecyclerViewAdapter
                .OnClickListener() {
            @Override
            public void setItem(String item) {
                // for switch item
                showNextViewSwitcher(item);
            }
        });

        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        
    }
    private void showNextViewSwitcher(String item) {
        mEventType = item;
        if (mViewSwitcher != null) {
            mViewSwitcher.showNext();
            mTypeTextView.setText(mEventType);
            mEventTypeImg.setImageDrawable(ContextCompat.getDrawable(getContext(),Config.trafficMap.get(mEventType)));

        }
    }
    //把里面的发生的事送到外面去
    private void setUpEventSpecs(final View dialogView) {
        mImageCamera = (ImageView) dialogView.findViewById(R.id.event_camera_img);
        mBackButton = (Button) dialogView.findViewById(R.id.event_back_button);
        mSendButton = (Button) dialogView.findViewById(R.id.event_send_button);
        mCommentEditText = (EditText) dialogView.findViewById(R.id.event_comment);
        mEventTypeImg = (ImageView) dialogView.findViewById(R.id.event_type_img);
        mTypeTextView = (TextView) dialogView.findViewById(R.id.event_type);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogCallBack.onSubmit(mCommentEditText.getText().toString(),
                        mEventType);
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewSwitcher.showPrevious();
            }
        });
    }


}
