package com.mandeep.imagesearch.ui

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.GridView
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mandeep.imagesearch.R
import com.mandeep.imagesearch.adapter.GridImageAdapter
import com.mandeep.imagesearch.model.Photo
import com.mandeep.imagesearch.utilities.Common
import com.mandeep.imagesearch.viewmodel.MainViewModel
import com.mandeep.imagesearch.viewmodelfactory.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), AbsListView.OnScrollListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var gridImageAdapter: GridImageAdapter
    private var photoList = mutableListOf<Photo>()
    private lateinit var gridViewMain: GridView
    private var isScrolling = true
    private var dataRequestedByScrolling = false
    private var lastSearchedTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init viewModel
        initViewModel()

        // setUp gridView
        setUpGridView()

        // observe changes in photos
        mainViewModel.photos.observe(this, Observer { photoList ->
            if (photoList != null && photoList.isNotEmpty()) {
                photoList.let {
                    this.photoList.clear()
                    this.photoList.addAll(it)
                    gridImageAdapter.updatePhotoList(it as ArrayList<Photo>)
                }
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        })

        searchBtn.setOnClickListener {
            hideKeyBoard()
            rootView.requestFocus()
            val queryText = searchEt.text.toString().trim()
            if (queryText.isNotEmpty()) {
                photoList.clear()
                getPhotos(queryText)
            } else Toast.makeText(this, "Please enter proper search item", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpGridAdapter() {
        gridImageAdapter = GridImageAdapter(this, R.layout.grid_image_item, photoList as ArrayList<Photo>)
        gridViewMain.apply {
            adapter = gridImageAdapter
            setOnScrollListener(this@MainActivity)
        }
    }

    private fun setUpGridView() {
        gridViewMain = gridView
        setColumns(mainViewModel.getSelectedNoOfColumns())

        // set gridAdapter
        setUpGridAdapter()
    }

    private fun setColumns(numberOfColumn: Int) {
        mainViewModel.setNumberOfColumns(numberOfColumn)
        val gridWidth = resources.displayMetrics.widthPixels
        val columnWidth = gridWidth / numberOfColumn
        gridViewMain.columnWidth = columnWidth
        gridViewMain.numColumns = numberOfColumn
    }

    private fun hideKeyBoard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun getPhotos(queryText: String) {
        mainViewModel.setLastQueriedText(queryText)
        if (Common.hasInternetConnection(this)) {
            mainViewModel.getPhotosFromFlickr(queryText)
        } else {
            if (dataRequestedByScrolling) {
                dataRequestedByScrolling = false
                // if user get the data and intentionally turned off internet, then no need to get data from local.
                // because that is same as displayed on screen
                Toast.makeText(this, "Internet is not working", Toast.LENGTH_SHORT).show()
                return
            }
            Toast.makeText(this, "Getting data from local", Toast.LENGTH_SHORT).show()
            mainViewModel.getPhotosFromLocal(queryText)
        }
    }

    private fun initViewModel() {
        val mainViewModelFactory = MainViewModelFactory(application)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.column_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.twoColumnMenu -> setColumns(2)
            R.id.threeColumnMenu -> setColumns(3)
            R.id.fourColumnMenu -> setColumns(4)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        // subtracting 2 for getting early data, so user don't have to wait in low network also
        if (!isScrolling && (firstVisibleItem + visibleItemCount >= totalItemCount - 2) && (System.currentTimeMillis() - lastSearchedTime > 2500)) {
            dataRequestedByScrolling = true

            getPhotos(mainViewModel.getLastQueriedText())
            isScrolling = true
            lastSearchedTime = System.currentTimeMillis()

        }

    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        isScrolling = scrollState == ScrollView.SCREEN_STATE_ON
    }

}