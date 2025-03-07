package com.eric.pageing3

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eric.base.data.Product
import com.eric.base.data.ProductDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ProductPagingSource"
private const val STARTING_PAGE_INDEX = 0

class ProductPagingSource(private val productDao: ProductDao) : PagingSource<Int, Product>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val pageIndex = params.key ?: 0
        val pageSize = params.loadSize

        return try {
            // ✅ 使用 `withContext(Dispatchers.IO)` 在 IO 线程查询数据库
            val products = withContext(Dispatchers.IO) {
                productDao.getPagedProducts(pageSize, pageIndex * pageSize)
            }

            Log.d(TAG, "加载第 $pageIndex 页，每页 $pageSize 条数据：${products.map { it.name }}")

            LoadResult.Page(
                data = products,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (products.isEmpty()) null else pageIndex + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "分页加载失败", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

