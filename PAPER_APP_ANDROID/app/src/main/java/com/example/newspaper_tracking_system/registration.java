package com.example.newspaper_tracking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener
{
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    Button b1;
    ImageView i1;
    SharedPreferences sh;
    Spinner s1;
    String[] gen={"Female","Male"};
    Bitmap bitmap=null;
    ProgressDialog pd=null;
    String selgen="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        e1=(EditText)findViewById(R.id.editText11);
        e2=(EditText)findViewById(R.id.editText7);
        e3=(EditText)findViewById(R.id.editText12);
        e4=(EditText)findViewById(R.id.editText13);
        e5=(EditText)findViewById(R.id.editText14);
        e6=(EditText)findViewById(R.id.editText15);
        e7=(EditText)findViewById(R.id.editText17);
        e8=(EditText)findViewById(R.id.editText18);
        s1=(Spinner)findViewById(R.id.spinner2);
        i1=(ImageView)findViewById(R.id.imageView2);

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

        b1=(Button)findViewById(R.id.button15);
        ArrayAdapter<String> ad=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,gen);
        s1.setAdapter(ad);
        s1.setOnItemSelectedListener(this);
        b1.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        selgen=gen[i];
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    { }

    @Override
    public void onClick(View view)
    {
        int f=0;
        if (e1.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e1.setError("enter data");
        }
        if (e2.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e2.setError("enter data");
        }
        if (e3.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e3.setError("enter data");
        }
        if (e4.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e4.setError("enter data");
        }
        if (e5.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e5.setError("enter data");
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(e6.getText().toString()).matches())
        {
            f=1;
            e6.setError("invalid email id");
        }
        if(e5.getText().toString().length()!=6)
        {
            f=1;
            e5.setError("invalid pin number");
        }
        if (e7.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e7.setError("enter data");
        }
        if(e7.getText().toString().length()!=10)
        {
            f=1;
            e7.setError("invalid phone number");
        }
        if (e8.getText().toString().equalsIgnoreCase(""))
        {
            f=1;
            e8.setError("enter data");
        }

        if (bitmap==null)
        {
            f=1;
            Toast.makeText(this, "Choose image...", Toast.LENGTH_SHORT).show();
        }
        if (f==0)
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
        pd=new ProgressDialog(registration.this);
        pd.setMessage("Uploading....");
        pd.show();

        final SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ip", "");
        String url = "http://" + ip + ":5000/and_user_registration";

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
                                Toast.makeText(registration.this, "Registration success...!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),login.class));
                            }
                            else
                                {
                                Toast.makeText(registration.this, "try again later", Toast.LENGTH_SHORT).show();
                                }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                            pd.dismiss();
                            Toast.makeText(registration.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("name", e1.getText().toString());
                params.put("place", e2.getText().toString());
                params.put("post", e3.getText().toString());
                params.put("district", e4.getText().toString());
                params.put("pin", e5.getText().toString());
                params.put("email", e6.getText().toString());
                params.put("number", e7.getText().toString());
                params.put("password", e8.getText().toString());
                params.put("gender", selgen);
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
}

//#################