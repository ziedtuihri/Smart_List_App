package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.zxing.Result;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class SimpleAndroidSearchViewExample extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    SearchView searchView;
    Button b, button;
    Dialog myDialog;
    ListView lstView;
    private ZXingScannerView scannerview;
    private Button scan;
    TextView title;
    String Unit ;
    ListView listView ;
    CustomListAdapterListProduct adapter;
    TextView linearLayout;
    String titre;
    MyDBHandlerlist dbHandler;
    MyDBHandler handl;
    int pos ;
    double S;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview_android_example);
        handl = new MyDBHandler(this, null, null, 1);

        button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                try{
                    startActivityForResult(intent,200);
                }catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
                }
            }
        });


        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");
        b=(Button)findViewById(R.id.addProduct);
        b.setOnClickListener(this);
        scan=(Button)findViewById(R.id.scan);
        scan.setOnClickListener(this);
        myDialog = new Dialog(this);
        title=(TextView)findViewById(R.id.title);
        Intent i = getIntent();
        titre=i.getStringExtra("title");
        title.setText(i.getStringExtra("title"));
        List<Product> li=new ArrayList<Product>();
        linearLayout=(TextView)findViewById(R.id.modecourse) ;
        linearLayout.setOnClickListener(this);


         dbHandler = new MyDBHandlerlist(this, null, null, 1);

        ProgressDialog pDialog = new ProgressDialog(this);
        List<ListProduct> productList = dbHandler.fetchlistdetail(i.getStringExtra("title"));
        adapter = new CustomListAdapterListProduct(this, li);
        listView = (ListView) findViewById(R.id.listProduct);
        listView.setAdapter(adapter);

        String ch = null;

        JSONObject js = null;
            JSONArray jr = new JSONArray();
            for (int k = 0; k < productList.size(); k++) {

                ch = productList.get(k).getList();
                System.out.println(ch);
                if (ch != null) {
                    try {
                        js = new JSONObject(ch);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jr = js.getJSONArray("list");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SimpleAndroidSearchViewExample.this, "Add Product to u list", Toast.LENGTH_LONG).show();
                }
            }

                if (ch != null) {
                    String name = null;
                    int prix = 0;
                    int f = 0;
                    String unite =null;
                    float quanti = 0;

                    while (f < jr.length()) {
                        try {
                            name = (String.valueOf(jr.get(f)));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            prix = (Integer) jr.get(f + 1);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            unite = (String) jr.get(f + 2);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            quanti = (Integer) jr.get(f + 3);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Product p = new Product();
                        p.set_productname(name);
                        p.setPrix(prix);
                        p.setQuantite(quanti);
                        p.setUnite(unite);
                        li.add(p);
                        f = f + 4;

                    }
                    for (int d = 0; d < li.size(); d++) {
                        System.out.println(li.get(d).get_productname());
                    }

                    TextView pr=(TextView)findViewById(R.id.price);
                    S=0;
                    for(int e=0;e<adapter.getCount();e++){
                        Product p= (Product) adapter.getItem(e);
                        S=S+p.getQuantite()*p.getPrix();
                    }

                    TextView number=(TextView)findViewById(R.id.number);
                    int nb = adapter.getCount();

                    pr.setText(String.valueOf(S));
                    number.setText(String.valueOf(nb));
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                else{
                    Toast.makeText(SimpleAndroidSearchViewExample.this,"Add Product to u list",Toast.LENGTH_LONG).show();
                }


        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {
                                adapter.remove(position);


                                Intent it = getIntent();

                                List<ListProduct> productList = dbHandler.fetchlistdetail(it.getStringExtra("title"));

                                JSONObject js = null;
                                JSONArray jsonarray =new JSONArray();

                                for (int k=0;k<productList.size();k++){

                                    String ch=productList.get(k).getList();
                                        try {
                                            js = new JSONObject(ch);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            jsonarray = js.getJSONArray("list");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                }
                                JsonArray jr=new JsonArray();
                                for(int r=0;r<jsonarray.length();r++){
                                    if(r==position*4){
                                       for(int e=0;e<4;e++){
                                           jsonarray.remove(position*4);
                                       }
                                    }
                                }


                                JSONObject o=new JSONObject();
                                try {
                                    o.put("list", jsonarray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //send variable to database "sqllite"

                                System.out.println(o);
                                ListProduct l=new ListProduct();
                                Intent i = getIntent();
                                l.setName(i.getStringExtra("title"));
                                l.setList(o.toString());
                                dbHandler.editList(l);





                            }
                        });
        listView.setOnTouchListener(touchListener);
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {


                    System.out.println(Unit);
                    Object selectedItem = (Product) listView.getItemAtPosition(position);
                    Product product = (Product) selectedItem;
                    String name=product.get_productname();
                    System.out.println(product.get_productname());
                    String prs= String.valueOf(product.getPrix());
                    Float quanit=product.getQuantite();
                    ShowPopupedit3(name ,prs,quanit);
                    pos=position;

                }
            }
        });



        final MyDBHandler mih=new MyDBHandler(this, null, null, 1);
        lstView = (ListView)findViewById(R.id.listsearsh);
        lstView.setOnItemClickListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length()!=0) {
                    ArrayList<String> product = new ArrayList<String>(mih.findProduct(query));
                    ArrayAdapter adapter = null;
                    lstView = (ListView) findViewById(R.id.listsearsh);
                    if (product != null) {
                        adapter = new ArrayAdapter(SimpleAndroidSearchViewExample.this, android.R.layout.simple_list_item_1, product);
                        lstView.setAdapter(adapter);

                    }
                }
                else {

                    lstView.setAdapter(null);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                if (newText.length()!=0) {
                    ArrayList<String> product = new ArrayList<String>(mih.findProduct(newText));
                    ArrayAdapter adapter = null;
                    lstView = (ListView) findViewById(R.id.listsearsh);
                   if (product != null) {
                        adapter = new ArrayAdapter(SimpleAndroidSearchViewExample.this, android.R.layout.simple_list_item_1, product);
                        lstView.setAdapter(adapter);
                    }

                }
                else {
                    lstView.setAdapter(null);

                }
                return false;

            }
        });






           List<Product> lp=new ArrayList<Product>();
            Product pr1=new Product();

            pr1.setCode("4523698745");
            pr1.setPrix(12);
            pr1.set_productname("fraise");
            lp.add(pr1);

        Product pr2=new Product();
        pr2.setCode("54662158415");
        pr2.setPrix(17);
        pr2.set_productname("lait");
        lp.add(pr2);




        Product pr10=new Product();
        pr10.setPrix(3);
        pr10.set_productname("bananne");
        lp.add(pr10);


        Product pr9=new Product();
        pr9.setPrix(4);
        pr9.set_productname("pomme de terre");
        lp.add(pr9);



        Product pr8=new Product();
        pr8.setPrix(4);
        pr8.set_productname("coffe");
        lp.add(pr8);




        Product pr7=new Product();
        pr7.setPrix(3);
        pr7.set_productname("jus");
        lp.add(pr7);





        Product pr6=new Product();
        pr6.setPrix(2);
        pr6.set_productname("biscuit");
        lp.add(pr6);




        Product pr5=new Product();
        pr5.setPrix(1);
        pr5.set_productname("eau");
        lp.add(pr5);




        Product pr4=new Product();
        pr4.setPrix(2);
        pr4.set_productname("tomate");
        lp.add(pr4);


        Product pr3=new Product();

        pr3.setCode("123456789");
        pr3.setPrix(15);
        pr3.set_productname("pomme");
        lp.add(pr3);

        for(int c=0;c<lp.size();c++) {
            Product pp = handl.findProductdatilwithname(lp.get(c).get_productname());
            if (pp.get_productname() == null) {
                if (lp.get(c).getCode()==null) {
                    handl.addProduct(lp.get(c));
                }
                else {
                    handl.addProductwithcode(lp.get(c));
                }
            }
        }





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchView.setQuery(result.get(0),false);
            }
        }
    }



    public void scancode(){
        scannerview =new ZXingScannerView(this);
        scannerview.setResultHandler(new ZXingScannerResultHandler());
        setContentView(scannerview);
        scannerview.startCamera();
    }

    public void onPause() {
        super.onPause();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler
    {

        @Override
        public void handleResult(Result result) {
            String ch=result.getText();
            System.out.println(ch);

            Product p=handl.findProductdatilwithcode(ch);
            System.out.println(ch);
            System.out.println(p.get_productname());
            SimpleAndroidSearchViewExample Simp =new SimpleAndroidSearchViewExample();
            setContentView(R.layout.searchview_android_example);
            Simp.onRestart();
            ShowPopupedit(p.get_productname(), String.valueOf(p.getPrix()));

        }

    }




    //pupup of create Product
    public void ShowPopup() {

        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.addproduct);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");

        Button add=(Button)myDialog.findViewById(R.id.add);
        add.setOnClickListener(this);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


    @Override
    public void onClick(View v) {
        ProgressDialog pDialog = new ProgressDialog(this);
        List<Product> li = new ArrayList<Product>();

        CustomListAdapterListProduct adapter = new CustomListAdapterListProduct(this, li);
        ListView listView = (ListView) findViewById(R.id.listProduct);
        JSONObject  o =new JSONObject ();
        JSONArray  a =new JSONArray ();
        switch (v.getId()) {
            case R.id.addProduct:

                ShowPopup();
                break;
            case R.id.add:
                EditText name=(EditText)myDialog.findViewById(R.id.nameproduct);
                EditText prix=(EditText)myDialog.findViewById(R.id.prix);
                System.out.print(name+" "+prix);
                MyDBHandler mh=new MyDBHandler(this, null, null, 1);
                int quantity =Integer.parseInt(prix.getText().toString());
                String namee =name.getText().toString();
                Product product =
                        new Product(namee, quantity);
                if(mh.findProduct(namee).isEmpty()){
                    mh.addProduct(product);
                    name.setText("");
                    prix.setText("");
                    myDialog.hide();
                }else {
                    Toast.makeText(getBaseContext(), "This product is exist", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.scan:

               scancode();
                break;
            case R.id.addtolist:
                MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);
                EditText nam=(EditText)myDialog.findViewById(R.id.nameproduct);
                EditText pri=(EditText)myDialog.findViewById(R.id.prix);
                Spinner Unites=(Spinner) myDialog.findViewById(R.id.unite);
                EditText quantite=(EditText)myDialog.findViewById(R.id.quantite);
                float qt=Float.parseFloat((quantite.getText()).toString());
                Unites.setOnItemSelectedListener(this);
                Float f= Float.parseFloat(pri.getText().toString());
                int b=(int) Math.round(f);


                Intent it = getIntent();

                List<ListProduct> productList = dbHandler.fetchlistdetail(it.getStringExtra("title"));

                JSONObject js = null;
                JSONArray jsonarray =new JSONArray();

                for (int k=0;k<productList.size();k++){

                    String ch=productList.get(k).getList();
                    if (ch != null) {
                        try {
                            js = new JSONObject(ch);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            jsonarray = js.getJSONArray("list");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                            js = new JSONObject();
                        try {
                            jsonarray = js.getJSONArray("list");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }


                jsonarray.put(nam.getText().toString());
                jsonarray.put(b);
                jsonarray.put("kg");

                try {
                    jsonarray.put(qt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    o.put("list", jsonarray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //send variable to database "sqllite"

                System.out.println(o);
                ListProduct l=new ListProduct();
                Intent i = getIntent();
                l.setName(i.getStringExtra("title"));
                l.setList(o.toString());
               dbHandler.editList(l);


               //get date to adapter to show the list
                String nameproduct = null;
                int prixproduct = 0;
                int conteur =0;
                String unite =null;
                Float quant = null;
                while (conteur<jsonarray.length()){

                    try {
                        nameproduct =(String.valueOf(jsonarray.get(conteur)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        prixproduct=(Integer) jsonarray.get(conteur+1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        unite=(String) jsonarray.get(conteur+2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        quant=Float.parseFloat(jsonarray.getString(conteur+3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Product pi = new Product() ;
                    pi.set_productname(nameproduct);
                    pi.setPrix(prixproduct);
                    pi.setUnite(unite);
                    pi.setQuantite(quant);
                    li.add(pi);
                    conteur=conteur+4;
                    TextView pr=(TextView)findViewById(R.id.price);
                    TextView number=(TextView)findViewById(R.id.number);
                    int nb = adapter.getCount();
                    double S=0;
                    for(int e=0;e<adapter.getCount();e++){
                        Product p= (Product) adapter.getItem(e);
                        S=S+p.getQuantite()*p.getPrix();
                    }
                    pr.setText(String.valueOf(S)+" ");
                    number.setText(String.valueOf(nb)+" ");

                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                myDialog.hide();
                break;
            case R.id.modecourse:

                Intent intent = new Intent(SimpleAndroidSearchViewExample.this, modecourseActivity.class);
                intent.putExtra("title", titre);
                startActivity(intent);

                break;
            case R.id.editlist:
                MyDBHandlerlist dbHandlerr = new MyDBHandlerlist(this, null, null, 1);

                EditText namm=(EditText)myDialog.findViewById(R.id.nameproduct);
                EditText prii=(EditText)myDialog.findViewById(R.id.prix);
                Spinner Unitee=(Spinner) myDialog.findViewById(R.id.unite);
                EditText quantitee=(EditText)myDialog.findViewById(R.id.quantite);

                Intent intt = getIntent();

                List<ListProduct> plist = dbHandlerr.fetchlistdetail(intt.getStringExtra("title"));

                JSONObject jso = null;
                JSONArray jsr =new JSONArray();

                for (int k=0;k<plist.size();k++){

                    String ch=plist.get(k).getList();
                    try {
                        jso = new JSONObject(ch);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        jsr = jso.getJSONArray("list");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                for(int r=0;r<jsr.length();r++){
                    if(r==pos*4){
                        for(int e=0;e<4;e++){
                            jsr.remove(pos*4);
                        }

                            jsr.put(namm.getText());

                        Float fp= Float.parseFloat(prii.getText().toString());
                        int fpr=(int) Math.round(fp);

                        jsr.put(fpr);


                        jsr.put("kg");

                        Float fq= Float.parseFloat(quantitee.getText().toString());
                        int fqt=(int) Math.round(fq);
                            jsr.put(fqt);





                    }
                }


                JSONObject ob=new JSONObject();
                try {
                    ob.put("list", jsr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //send variable to database "sqllite"

                System.out.println(ob);
                ListProduct lists=new ListProduct();
                Intent lntentt = getIntent();
                lists.setName(lntentt.getStringExtra("title"));
                lists.setList(ob.toString());
                dbHandlerr.editList(lists);
                myDialog.hide();

                //get date to adapter to show the list
                 nameproduct = null;
                prixproduct = 0;
                conteur =0;
                unite =null;
                quant = null;
                while (conteur<jsr.length()){

                    try {
                        nameproduct =(String.valueOf(jsr.get(conteur)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        prixproduct= (int) Integer.parseInt(String.valueOf(jsr.get(conteur+1)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        unite=(String) jsr.get(conteur+2);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        quant=Float.parseFloat(jsr.getString(conteur+3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Product pi = new Product() ;
                    pi.set_productname(nameproduct);
                    pi.setPrix(prixproduct);
                    pi.setUnite(unite);
                    pi.setQuantite(quant);
                    li.add(pi);
                    conteur=conteur+4;
                    TextView pr=(TextView)findViewById(R.id.price);
                    TextView number=(TextView)findViewById(R.id.number);
                    int nb = adapter.getCount();
                    double S=0;
                    for(int e=0;e<adapter.getCount();e++){
                        Product p= (Product) adapter.getItem(e);
                        S=S+p.getQuantite()*p.getPrix();
                    }
                    pr.setText(String.valueOf(S)+" Price");
                    number.setText(String.valueOf(nb)+" Product");

                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



                break;


        }
    }
    //pupup of create group
    public void ShowPopupedit(String name,String prix) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.add_product_to_list);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        Spinner spinner = (Spinner) myDialog.findViewById(R.id.unite);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        final EditText qttt=(EditText)myDialog.findViewById(R.id.quantite);
        final EditText nam=(EditText)myDialog.findViewById(R.id.nameproduct);
        final EditText pri=(EditText)myDialog.findViewById(R.id.prix);
        Button add=(Button)myDialog.findViewById(R.id.addtolist);
        final Switch bamount=(Switch)myDialog.findViewById(R.id.buttonamount);
        final TextView tamount=(TextView) myDialog.findViewById(R.id.textamount);
        bamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bamount.isChecked()){
                    float f1=Float.parseFloat(String.valueOf(pri.getText()));
                    System.out.println(f1);
                    float f2=Float.parseFloat(String.valueOf(qttt.getText()));

                    System.out.println(f2);

                    float somme=f1*f2;
                    tamount.setText("Price of " +String.valueOf(f2)+" "+nam.getText()+" = "+String.valueOf(somme)+" DT");

                }
                else {
                    float f2=Float.parseFloat(String.valueOf(qttt.getText()));
                    tamount.setText("Price of 1 "+nam.getText()+" = "+pri.getText()+" DT");

                }
            }
        });




        add.setOnClickListener(this);
        nam.setText(name);
        pri.setText(prix);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
    public void ShowPopupedit3(final String name, final String prix, final Float quantite) {

        final TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.edit_product_in_list);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");
        Spinner spinner = (Spinner) myDialog.findViewById(R.id.unite);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);



        Button add=(Button)myDialog.findViewById(R.id.editlist);
        add.setOnClickListener(this);
        final EditText nam=(EditText)myDialog.findViewById(R.id.nameproduct);
        final EditText pri=(EditText)myDialog.findViewById(R.id.prix);
        final EditText qt=(EditText)myDialog.findViewById(R.id.quantite);

        final Switch bamount=(Switch) myDialog.findViewById(R.id.buttonamount);

        final TextView tamount=(TextView) myDialog.findViewById(R.id.textamount);
        bamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bamount.isChecked()){
                    float f1=Float.parseFloat(String.valueOf(pri.getText()));
                    System.out.println(f1);
                    float f2=Float.parseFloat(String.valueOf(qt.getText()));

                    System.out.println(f2);

                    float somme=f1*f2;
                    tamount.setText("Price of " +String.valueOf(f2)+" "+nam.getText()+" = "+String.valueOf(somme)+" DT");

                }
                else {
                    float f2=Float.parseFloat(String.valueOf(qt.getText()));
                    tamount.setText("Price of 1 "+nam.getText()+" = "+pri.getText()+" DT");

                }
            }
        });



        qt.setText((String.valueOf(quantite)));
        nam.setText(name);
        pri.setText(prix);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final MyDBHandler mih=new MyDBHandler(this, null, null, 1);

        String website = (String) lstView.getItemAtPosition(position);
        Product product = mih.findProductdatil(website);
        String name=product.get_productname();
        String prs= String.valueOf(product.getPrix());
        ShowPopupedit(name ,prs);

    }
}