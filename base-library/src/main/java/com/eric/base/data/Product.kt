package com.eric.base.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double
)

// ProductDao.kt
@Dao
interface ProductDao {

    // ✅ 正确的分页查询，使用 LIMIT OFFSET
    @Query("SELECT * FROM products ORDER BY id ASC LIMIT :limit OFFSET :offset")
    fun getPagedProducts(limit: Int, offset: Int): List<Product>

    // 保留 Flow 方式查询全部数据（非分页）
    @Query("SELECT * FROM products ORDER BY id ASC")
    fun getAllProductsAsFlow(): Flow<List<Product>>

    // 插入单个实体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    // 插入多个实体
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)
}

