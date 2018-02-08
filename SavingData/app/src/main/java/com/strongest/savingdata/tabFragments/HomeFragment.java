package com.strongest.savingdata.tabFragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.strongest.savingdata.Activities.WebViewActivity;
import com.strongest.savingdata.Adapters.ArticleAdapter;
import com.strongest.savingdata.Database.Managers.ArticleDataManager;
import com.strongest.savingdata.Database.Articles.ArticleObj;
import com.strongest.savingdata.Database.Articles.DownloadImage;
import com.strongest.savingdata.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.strongest.savingdata.Database.Articles.DBArticleHelper.TABLE_NAME;

/**
 * Created by Cohen on 4/22/2017.
 */

public class HomeFragment extends BaseTabs implements ArticleAdapter.RecyclerViewClickListener {


    private ArrayList<ArticleObj> texts = new ArrayList<>();
    private ArticleDataManager articleDataManager;
    private ContentLoadingProgressBar pb;
    private ArticleAdapter adapter;


    private static final String TAG = "aviv";

    private RecyclerView recycler;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.home, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_menu) {
            return true;
        }
        return false;
    }

    private void initView(final View v) {
        pb = (ContentLoadingProgressBar) v.findViewById(R.id.home_progress_bar);
       // articleDataManager = new ArticleDataManager(getContext());
        recycler = (RecyclerView) v.findViewById(R.id.home_recycler);
       // init();

    }

    public void dealWithProgressBar(boolean flag) {
        if (flag) {
            pb.setVisibility(View.VISIBLE);
        } else {
            pb.setVisibility(GONE);
        }
    }

    public void init() {
        texts = (ArrayList<ArticleObj>) articleDataManager.readByTable(TABLE_NAME);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new ArticleAdapter(getContext(), texts, this);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);
    }

    public void updateArticles() {
        texts = (ArrayList<ArticleObj>) articleDataManager.readByTable(TABLE_NAME);
        adapter.setTexts(texts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {
     /*  ArticleObj arObj = texts.get(position);
        WebViewFragment wvf = WebViewFragment.newInstance(arObj.getPage());
        getFragmentManager().beginTransaction().add(wvf,"article").commit();*/

        ArticleObj arObj = texts.get(position);
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(WebViewActivity.PAGE, arObj.getPage());
        startActivity(intent);

    }

    public class LoadArticles extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Object doInBackground(Object[] params) {
            ArticleObj arobj;
            try {
                Document doc = Jsoup.connect("http://strongest.co.il").get();
                final Elements article = doc.select("article");
                StringBuilder sb = new StringBuilder();
                for (Element link : article) {
                    arobj = new ArticleObj();
                    arobj.setTitle(link.select("a").attr("title"));
                    arobj.setSummary(link.select("p").text());
                    arobj.setLink(link.select("img").attr("src"));
                    new DownloadImage(arobj, articleDataManager).run();
                    texts.add(arobj);
                    //    Log.d("aviv", "doInBackground: " + arobj.getLink());
                    //    Log.d("aviv", "doInBackground: \n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            init();

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //articleDataManager.close();
    }
}
