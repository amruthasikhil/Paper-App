package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class add_my_classifieds extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
{
    Spinner s1;
    EditText e1,ed1,e2,e3;
    ImageView i1;
    Button b1,b2;
    ArrayList<String> lid,prname;
    SharedPreferences sh;

    Bitmap bitmap=null;
    ProgressDialog pd=null;
    String sel_provider="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_classifieds);
        s1=(Spinner)findViewById(R.id.spinner);
        e1=(EditText)findViewById(R.id.editText8);
        e2=(EditText)findViewById(R.id.editText9);
        e3=(EditText)findViewById(R.id.editText10);
        i1=(ImageView)findViewById(R.id.imageView4);

        i1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        }
        );

        b2=(Button)findViewById(R.id.button11);
        b2.setOnClickListener(this);
        s1.setOnItemSelectedListener(this);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ip", "");
        String url1 = "http://" + ip + ":5000/and_view_provider";
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest1 = new StringRequest(Request.Method.POST, url1,

                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok"))
                            {

                                JSONArray js= jsonObj.getJSONArray("data");
                                lid=new ArrayList<>();
                                prname=new ArrayList<>();

                                for(int i=0;i<js.length();i++){
                                    JSONObject u=js.getJSONObject(i);
                                    lid.add(u.getString("provider_login_id"));
                                    prname.add(u.getString("provider_name"));
                                }

                                ArrayAdapter<String> ad1=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,prname);
                                s1.setAdapter(ad1);

                            } else
                                {
                                Toast.makeText(getApplicationContext(), "No Providers", Toast.LENGTH_LONG).show();
                                }

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                )
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest1.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(postRequest1);
    }

    @Override
    public void onClick(View view)
    {
            int f = 0;
            final SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String ip = sh.getString("ip", "");
            final String description = e1.getText().toString();
            final String accno = e2.getText().toString();
            final String pin = e3.getText().toString();
            //Toast.makeText(getApplicationContext(),uid,Toast.LENGTH_SHORT).show();
            if (description.equalsIgnoreCase(""))
            {
                e1.setError("Null");
            }
            if (accno.equalsIgnoreCase(""))
            {
                e2.setError("Null");
            }
            if (pin.equalsIgnoreCase(""))
            {
                e3.setError("Null");
            }
            if (bitmap == null)
            {
                f = 1;
                Toast.makeText(this, "Choose image...", Toast.LENGTH_SHORT).show();
            }
            if (f == 0)
            {
                uploadBitmap();
            }
        }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            Uri imageUri = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                i1.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    private void uploadBitmap()
    {
        pd=new ProgressDialog(add_my_classifieds.this);
        pd.setMessage("Uploading....");
        pd.show();

        final SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/user_add_classifieds";

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>()
                {
                    @Override
                    public void onResponse(NetworkResponse response)
                    {
                        try
                        {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(new String(response.data));
                            if(obj.getString("status").equalsIgnoreCase("ok"))
                            {
                                Toast.makeText(add_my_classifieds.this, "Success...!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),UHOME.class));
                            }
                            else
                                {
                                Toast.makeText(add_my_classifieds.this, "try again later", Toast.LENGTH_SHORT).show();
                                }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(add_my_classifieds.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
                )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("lid", sh.getString("lid",""));
                params.put("provider", sel_provider);
                params.put("agent_id", sh.getString("agent_id",""));
                params.put("desc",e1.getText().toString());
                params.put("accno", e2.getText().toString());
                params.put("pin", e3.getText().toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData()
            {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        sel_provider=lid.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    { }
}

//###################