package com.example.shoppingcart;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.shoppingcart.storage.DBHelper;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity 
{
	public static final String M_CURRENT_TAB = "M_CURRENT_TAB";
    private TabHost mTabHost;
    private String mCurrentTab;
	private TextView titleTextView;

    public static final String TAB_STORE = "TAB_STORES";
    public static final String TAB_LIST = "TAB_LIST";
    public static final String TAB_CART = "TAB_CART";

    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);
        getActionBar().hide();
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        
        titleTextView = (TextView)findViewById(R.id.headingTextView);
        Button logOutButton = (Button)findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) 
			{
				//DBHelper dbHelper = new DBHelper(MainActivity.this);
				//dbHelper.deleteAllAddedItems();
				//dbHelper.deleteAllCartProducts();
				MainActivity.this.finish();
				Intent intent = new Intent(MainActivity.this,LoginActivity.class);
				startActivity(intent);
				
			}
		});
        
        if (savedInstanceState != null) 
        {
            mCurrentTab = savedInstanceState.getString(M_CURRENT_TAB);
            initializeTabs();
            mTabHost.setCurrentTabByTag(mCurrentTab);
            /*
            when resume state it's important to set listener after initializeTabs
            */
            mTabHost.setOnTabChangedListener(listener);
        } 
        else 
        {
            mTabHost.setOnTabChangedListener(listener);
            initializeTabs();
            /*Intent intent = getIntent();
            if(intent != null && intent.getStringExtra("Open").equals("Cart"))
            {
            	mTabHost.setCurrentTabByTag(TAB_CART);
            }*/
        }
    }
    
    
    
    private View createTabView(final int id, final String text)
    {
        View view = LayoutInflater.from(this).inflate(R.layout.tag_icon, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.tab_icon);
        imageView.setImageDrawable(getResources().getDrawable(id));
        TextView textView = (TextView) view.findViewById(R.id.tab_text);
        textView.setText(text);
        return view;
    }

    /*
    create 3 tabs with name and image
    and add it to TabHost
     */
    public void initializeTabs() 
    {
        TabHost.TabSpec spec;
        spec = mTabHost.newTabSpec(TAB_STORE);
        spec.setContent(new TabHost.TabContentFactory() 
        {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.store,"Stores"));
        mTabHost.addTab(spec);

        spec = mTabHost.newTabSpec(TAB_LIST);
        spec.setContent(new TabHost.TabContentFactory() {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.shoppinglist,"List"));
        mTabHost.addTab(spec);


        spec = mTabHost.newTabSpec(TAB_CART);
        spec.setContent(new TabHost.TabContentFactory()
        {
            public View createTabContent(String tag) {
                return findViewById(android.R.id.tabcontent);
            }
        });
        spec.setIndicator(createTabView(R.drawable.cart, "Cart"));
        mTabHost.addTab(spec);

    }

    /*
    first time listener will be trigered immediatelly after first: mTabHost.addTab(spec);
    for set correct Tab in setmTabHost.setCurrentTabByTag ignore first call of listener
    */
    TabHost.OnTabChangeListener listener = new TabHost.OnTabChangeListener() {
        public void onTabChanged(String tabId) {

            mCurrentTab = tabId;

            if (tabId.equals(TAB_STORE)) 
            {
            	titleTextView.setText("Stores");
                pushFragments(StoresFragment.getInstance(), false,
                        false, null);
            } 
            else if (tabId.equals(TAB_LIST)) 
            {
            	titleTextView.setText("List");
                pushFragments(ShoppingListFragment.getInstance(), false,
                        false, null);
            }
            else if (tabId.equals(TAB_CART)) 
            {
            	titleTextView.setText("Cart");
                pushFragments(CartFragment.getInstance(), false,
                        false, null);
            }
        }
    };

    public void pushFragments(Fragment fragment,boolean shouldAnimate, boolean shouldAdd, String tag) 
    {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, fragment, tag);

        if (shouldAdd) {
            /*
            here you can create named backstack for realize another logic.
            ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
            and remove named backstack:
            manager.popBackStack("name of your backstack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            or remove whole:
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    /*
    If you want to start this activity from another
     */
   /* public static void startUrself(Activity context) 
    {
        Intent newActivity = new Intent(context, TagsActivity.class);
        newActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newActivity);
        context.finish();
    }*/

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(M_CURRENT_TAB, mCurrentTab);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed(){
    }
}
