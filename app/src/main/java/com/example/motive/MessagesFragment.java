package com.example.motive;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


public class MessagesFragment extends Fragment {

    public MessagesFragment() {
        // Required empty public constructor
    }

    private WebView webView;
    private TextView messages;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        FindViewById(view);
        return view;

    }


    private void FindViewById(View view)
    {

        webView = view.findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://www.instagram.com/?hl=en");

        messages = view.findViewById(R.id.messagesTextView);

        messages.setPaintFlags(messages.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
    }
}


