package com.eric.pageing3

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eric.base.data.Product
import com.eric.base.db.AppDatabase
import kotlinx.coroutines.flow.Flow

// ProductViewModel.kt
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productRepository: ProductRepository

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        productRepository = ProductRepository(productDao)
    }

    val productPagingData: Flow<PagingData<Product>> = productRepository.getAllProducts()
        .cachedIn(viewModelScope)
}
