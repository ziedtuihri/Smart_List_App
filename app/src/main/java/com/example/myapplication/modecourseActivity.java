package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.JsonArray;
import com.google.zxing.Result;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.widget.Toast.LENGTH_SHORT;


public class modecourseActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    SearchView searchView;
    Button b;
    CustomListAdapterListProduct adapterr;
    Dialog myDialog;
    ListView lstView;
    private ZXingScannerView scannerview;
    private Button scan;
    String Unit ;
    ListView listView ;
    CustomListAdapterListProduct_course_mode adapter;
    LinearLayout buy;
    double S =0;
    int c=0;
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modecourse);
        scan=(Button)findViewById(R.id.scan);
        scan.setOnClickListener(this);
        b=(Button)findViewById(R.id.addProduct);
        b.setOnClickListener(this);
        searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");
        myDialog = new Dialog(this);
        Intent i = getIntent();
        List<Product> li=new ArrayList<Product>();
        listView = (ListView) findViewById(R.id.listProduct);
        final MyDBHandler mih=new MyDBHandler(this, null, null, 1);

        buy=(LinearLayout)findViewById(R.id.buy) ;
        buy.setOnClickListener(this);
        final List<Product> lp=new ArrayList<Product>();
        adapterr = new CustomListAdapterListProduct(modecourseActivity.this, lp);

        listView = (ListView) findViewById(R.id.listProduct);
        listView = (ListView) findViewById(R.id.listProduct);
        myDialog.setContentView(R.layout.addproduct);



        MyDBHandlerlist dbHandler = new MyDBHandlerlist(this, null, null, 1);
        ProgressDialog pDialog = new ProgressDialog(this);
        List<ListProduct> productList = dbHandler.fetchlistdetail(i.getStringExtra("title"));
        adapter = new CustomListAdapterListProduct_course_mode(this, li);
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
                Toast.makeText(modecourseActivity.this, "Add Product to u list", Toast.LENGTH_LONG).show();
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



            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        else{
            Toast.makeText(modecourseActivity.this,"Add Product to u list",Toast.LENGTH_LONG).show();
        }

        //add Product to panier with swipe
        //final List<Product> listpanier=new ArrayList<Product>();
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

                                Object selectedItem = (Product) listView.getItemAtPosition(position);
                                Product website = (Product) selectedItem;
                                String name=website.get_productname();
                                String prs= String.valueOf(website.getPrix());
                                String unite=website.getUnite();
                                String quantite=String.valueOf(website.getQuantite());
                                Product p=new Product();
                                p.set_productname(name);
                                p.setPrix((int) Float.parseFloat((prs)));
                                p.setQuantite(Float.parseFloat(quantite));
                                p.setUnite(unite);
                                lp.add(p);
                                ListView panier=(ListView)findViewById(R.id.panier);
                                panier.setAdapter(adapterr);
                                TextView pr=(TextView)findViewById(R.id.price);
                                    System.out.println(S+" one");
                                    Product pe= (Product) adapterr.getItem(c);
                                    S=S+pe.getQuantite()*p.getPrix();
                                    System.out.println(S+" two");
                                c++;

                                pr.setText(String.valueOf(S)+" /DT");
                                TextView number=(TextView)findViewById(R.id.number);
                                int nb = adapterr.getCount();
                                number.setText(String.valueOf(nb)+" /Product");
                                adapterr.notifyDataSetChanged();
                                adapter.remove(position);
                                //Product product=(Product)adapter.getItem(position);
                                //listpanier.add(product);
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
                    pos=position;
                    ShowPopupedit3(name ,prs,quanit);






                }
            }
        });

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
                        adapter = new ArrayAdapter(modecourseActivity.this, android.R.layout.simple_list_item_1, product);
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
                        adapter = new ArrayAdapter(modecourseActivity.this, android.R.layout.simple_list_item_1, product);
                        lstView.setAdapter(adapter);
                    }

                }
                else {
                    lstView.setAdapter(null);

                }
                return false;

            }
        });

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
            ShowPopupedit(ch,"3");
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
    public void ShowPopupbuy() {

        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.activity_mainbuy);
        txtclose =(TextView) myDialog.findViewById(R.id.close);
        txtclose.setText("X");

        Button add=(Button)myDialog.findViewById(R.id.addbuy);
        add.setOnClickListener(this);
        Button cancel=(Button)myDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
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
                    System.out.println(ch+"kilou");
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
                    pr.setText(String.valueOf(S)+" Price");
                    number.setText(String.valueOf(nb)+" Product");

                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                myDialog.hide();
                break;
                case R.id.buy:
                    if(adapterr.getCount()==0){
                        Toast.makeText(modecourseActivity.this,"No product in the basket", LENGTH_SHORT).show();
                    }else
                    {
                        ShowPopupbuy();

                    }
                    break;
            case R.id.addbuy:
                JSONArray jrs=new JSONArray();
                for(int j=0;j<adapterr.getCount();j++){
                    Product p= (Product) adapterr.getItem(j);
                    jrs.put(p.get_productname());
                    try {
                        jrs.put(p.getPrix());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jrs.put(p.getUnite());
                    try {
                        jrs.put(p.getQuantite());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    System.out.println(jrs);
                }

                i = getIntent();
                System.out.println(i.getStringExtra("title")+"hhhhhhhh");
                dbHandler = new MyDBHandlerlist(this, null, null, 1);

                productList = dbHandler.fetchlistdetail(i.getStringExtra("title"));

                js = null;
                jsonarray =new JSONArray();

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


                try {
                    o.put("list", jsonarray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //send variable to database "sqllite"

                System.out.println(o);
                ListProductHistorique lis=new ListProductHistorique();
                i = getIntent();
                lis.setName(i.getStringExtra("title"));
                lis.setList(o.toString());
                S=0;
                for(int e=0;e<adapterr.getCount();e++){
                    Product p= (Product) adapterr.getItem(e);
                    S=S+p.getQuantite()*p.getPrix();
                }
                int nb = adapterr.getCount();
                lis.setPrice((float) S);

                System.out.println(lis.getName()+"jjjjjj");
                System.out.println(lis.getList()+"fgdfgf");
                MyDBHandlerlistHistorique handlerhistorique =new MyDBHandlerlistHistorique(this, null, null, 1);
                    List<ListProductHistorique> lp=handlerhistorique.fetchlistdetail(i.getStringExtra("title"));
                    if(lp.isEmpty()){
                        handlerhistorique.addList(lis);
System.out.println("created");
                    }else {
                        handlerhistorique.deleteList(i.getStringExtra("title"));
                        handlerhistorique.addList(lis);

                    }
                myDialog.hide();



                break;
            case R.id.editlist:
                MyDBHandlerlist dbHandlerr = new MyDBHandlerlist(this, null, null, 1);

                EditText namm=(EditText)myDialog.findViewById(R.id.nameproduct);
                EditText prii=(EditText)myDialog.findViewById(R.id.prix);
                EditText quantitee=(EditText)myDialog.findViewById(R.id.quantite);

                Intent intent = getIntent();

                List<ListProduct> plist = dbHandlerr.fetchlistdetail(intent.getStringExtra("title"));

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



        Button add=(Button)myDialog.findViewById(R.id.addtolist);
        add.setOnClickListener(this);
        EditText nam=(EditText)myDialog.findViewById(R.id.nameproduct);
        EditText pri=(EditText)myDialog.findViewById(R.id.prix);
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
    public void ShowPopupedit3(String name,String prix,Float quantite) {

        TextView txtclose;
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
        EditText nam=(EditText)myDialog.findViewById(R.id.nameproduct);
        EditText pri=(EditText)myDialog.findViewById(R.id.prix);
        EditText qt=(EditText)myDialog.findViewById(R.id.quantite);

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