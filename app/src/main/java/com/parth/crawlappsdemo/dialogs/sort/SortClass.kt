package com.parth.crawlappsdemo.dialogs.sort

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.parth.crawlappsdemo.R
import com.parth.crawlappsdemo.databinding.AdapterSortBinding

class SortClass {

    private var adapterSort: AdapterSort? = null
    private val mList = ArrayList<SortModel>()

    @SuppressLint("InflateParams")
    fun createSortDialog(
        activity: AppCompatActivity,
        sortList: ArrayList<SortModel>,
        onSortClick: (Int) -> Unit = {}
    ): BottomSheetDialog {
        mList.clear()
        mList.addAll(sortList)

        val mBottomSheetDialogSorting = BottomSheetDialog(activity)

        val menuView = activity.layoutInflater.inflate(R.layout.dialog_bottom_sheet_sorting, null)

        val ivClose: AppCompatImageView = menuView.findViewById(R.id.ivClose)
        ivClose.setOnClickListener {
            mBottomSheetDialogSorting.hide()
        }
        val recyclerViewVariant: RecyclerView = menuView.findViewById(R.id.bottom_sheet_list)
        recyclerViewVariant.layoutManager = LinearLayoutManager(activity)
        adapterSort = AdapterSort(mList, onSortClick)
        recyclerViewVariant.adapter = adapterSort

        mBottomSheetDialogSorting.setContentView(menuView)

        return mBottomSheetDialogSorting
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: ArrayList<SortModel>) {
        mList.clear()
        mList.addAll(data)
        adapterSort?.notifyDataSetChanged()
    }

    class AdapterSort(
        private val mList: ArrayList<SortModel>,
        private val onSortClick: (Int) -> Unit = {}
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            SortViewHolder(
                AdapterSortBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val v = holder as SortViewHolder

            v.binding.ivCheck.isVisible = mList[position].isSelect
            v.binding.textViewSort.text = mList[position].name

            v.binding.root.setOnClickListener {
                onSortClick(position)
            }
        }

        override fun getItemCount() = mList.size

        class SortViewHolder(val binding: AdapterSortBinding) :
            RecyclerView.ViewHolder(binding.root)
    }
}