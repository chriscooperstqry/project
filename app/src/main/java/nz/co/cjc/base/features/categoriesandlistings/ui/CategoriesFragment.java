package nz.co.cjc.base.features.categoriesandlistings.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import nz.co.cjc.base.R;
import nz.co.cjc.base.features.categoriesandlistings.logic.CategoriesViewLogic;
import nz.co.cjc.base.features.categoriesandlistings.models.CategoryData;
import nz.co.cjc.base.framework.application.MainApp;
import nz.co.cjc.base.framework.constants.AppConstants;

/**
 * Created by Chris Cooper on 4/05/16.
 * <p>
 * Fragment to display the categories received from the api
 */
public class CategoriesFragment extends Fragment {

    private ListView mListView;
    private CategoriesAdapter mAdapter;

    @Inject
    CategoriesViewLogic mViewLogic;

    // region public
    @NonNull
    public static CategoriesFragment newInstance(@NonNull Bundle arguments) {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        categoriesFragment.setArguments(arguments);
        return categoriesFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUI(view);
        MainApp.getDagger().inject(this);
        mViewLogic.initViewLogic(mViewLogicDelegate, getArguments());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mViewLogic != null) {
            mViewLogic.screenResumed();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories_fragment, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mListView = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mViewLogic != null) {
            mViewLogic.disconnect();
            mViewLogic = null;
        }
    }

    @Nullable
    public ListView getListView() {
        return mListView;
    }
    //end region

    //region private
    private void initUI(View view) {
        mListView = ButterKnife.findById(view, R.id.list_view);
        mAdapter = new CategoriesAdapter();
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mViewLogic != null) {
                            mViewLogic.listItemSelected(position);
                        }
                    }
                }, AppConstants.RIPPLE_DELAY);

            }
        });

    }
    //end region

    private CategoriesViewLogic.ViewLogicDelegate mViewLogicDelegate = new CategoriesViewLogic.ViewLogicDelegate() {

        @Override
        public void populateScreen(@NonNull List<CategoryData> categories) {
            mAdapter.setCategoryItems(categories);
        }
    };

}
