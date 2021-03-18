package com.meowbeos.studentsupport.activity.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.meowbeos.studentsupport.R;
import com.meowbeos.studentsupport.adapter.news.RelatedNewsAdapter;
import com.meowbeos.studentsupport.constants.STConstant;
import com.meowbeos.studentsupport.model.DetailNews;
import com.meowbeos.studentsupport.service.ServiceAPI;
import com.meowbeos.studentsupport.utils.StringUtil;
import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDetailActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetail";
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ConstraintLayout constraintLayout;

    TextView title, content, author, datePost;
    ImageView image;
    RecyclerView recRelatedNews;
    RelatedNewsAdapter relatedNewsAdapter;
    private String idNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        initComponent();

        Bundle pack = getIntent().getBundleExtra("DATA");

        if (pack != null) {

            idNews = pack.getString("idNews");
            getDetailNews(StringUtil.generateAPIKey(), idNews);
        }

    }

    private void initComponent() {
        constraintLayout = findViewById(R.id.constraint_layout);

        title = findViewById(R.id.txtTitleNewsDetail);
        content = findViewById(R.id.txtContentDetailNews);
        image = findViewById(R.id.imgNewsDetail);
        author = findViewById(R.id.txtAuthorNewsDetail);
        datePost = findViewById(R.id.txtDatePostNewsDetail);
        recRelatedNews = findViewById(R.id.recRelatedNews);
    }

    private void getDetailNews(String apiKey, String idNews) {
        ServiceAPI serviceAPI = new Retrofit.Builder()
                .baseUrl(STConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceAPI.class);

        compositeDisposable.add(serviceAPI.detailNews(apiKey, idNews)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Log.e(TAG, "handleError: Error", throwable);
    }

    private void handleResponse(DetailNews detailNews) {
        title.setText(detailNews.getItemNews().getTitleNews());
        content.setText(Html.fromHtml(detailNews.getItemNews().getContentNews()));
        author.setText(detailNews.getItemNews().getAuthorNews());
        datePost.setText(detailNews.getItemNews().getDatePost());
        Picasso.get().load(detailNews.getItemNews().getUrlBanner()).error(R.drawable.error).into(image);

        relatedNewsAdapter = new RelatedNewsAdapter(NewsDetailActivity.this, detailNews);
        recRelatedNews.setAdapter(relatedNewsAdapter);
        recRelatedNews.setLayoutManager(new LinearLayoutManager(NewsDetailActivity.this));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}