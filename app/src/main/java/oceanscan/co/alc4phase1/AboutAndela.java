package oceanscan.co.alc4phase1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.material.snackbar.Snackbar;
import oceanscan.co.alc4phase1.utils.ConnectionDetector;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class AboutAndela extends AppCompatActivity {
    String url="https://andela.com/alc/";
    CoordinatorLayout layout;
    private WebView webview;
    private ConnectionDetector connection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_andela);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });
        webview=findViewById(R.id.webview_about_andela);
        connection=new ConnectionDetector(AboutAndela.this);
        layout=findViewById(R.id.layout_about_andela);
        startWebView(url);
    }
    private void startWebView(String url) {
        boolean connected = connection.isConnectingToInternet();
        if (connected) {
            webview.setWebViewClient(new WebViewClient() {
                ProgressDialog progressDialog;

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onLoadResource(WebView view, String url) {
                    if (progressDialog == null) {
                        progressDialog = new ProgressDialog(AboutAndela.this);
                        progressDialog.setCancelable(true);
                         progressDialog.setMessage("Loading Andela's info. Please wait...");
                        progressDialog.show();
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    String message = "SSL Certificate error.";
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = "The certificate authority is not trusted.";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "The certificate has expired.";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "The certificate Hostname mismatch.";
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = "The certificate is not yet valid.";
                            break;
                    }
                    message += "\"SSL Certificate Error\" Do you want to continue anyway?.. YES";
                    Log.i("MainActivity",message);
                    handler.proceed(); // Ignore SSL certificate errors
                }
            });
            webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            webview.setScrollbarFadingEnabled(false);
            webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webview.getSettings().setAllowFileAccess(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setSupportZoom(true);
            webview.loadUrl(url);
        } else {
            Snackbar snackbar = Snackbar.make(layout,"Poor internet connection.",Snackbar.LENGTH_SHORT);
            snackbar.show();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(AboutAndela.this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(AboutAndela.this);
            }
            builder.setTitle("Network failure")
                    .setMessage("Poor internet connection. Make sure you have a working internet connection and click try again")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startWebView(URL);
                        }
                    })
                    .setNegativeButton("Close ", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(R.drawable.ic_warning)
                    .show();
        }
    }
}
