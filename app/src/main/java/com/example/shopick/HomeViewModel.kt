package com.example.shopick

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopick.datamodels.Store
import com.example.shopick.utils.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class HomeViewModel : ViewModel() {

    private var mDatabaseReference: DatabaseReference? = null
    var storeItems: MutableLiveData<List<Store>>?= null

    fun getStoresList() : LiveData<List<Store>> {
        if (storeItems == null) {
            storeItems = MutableLiveData()
            getStores()
        }
        return storeItems!!
    }

    private fun getStores(){

        val list =  arrayListOf<Store>()

        mDatabaseReference =
            FirebaseUtil.getDatabase().getReference("Stores")

        mDatabaseReference!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for (items in snapshot.children) {
                        val item = items.getValue(Store::class.java)
                        item?.let { list.add(it) }
                    }
                    storeItems?.postValue(list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Stores", error.toString())
                }

            })
    }
}