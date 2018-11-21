package com.stanledelacruz.listmaker

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class ListDetailFragment : Fragment() {

    lateinit var list: TaskList
    private lateinit var listItemsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list = arguments!!.getParcelable(ListActivity.INTENT_LIST_KEY)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_detail, container, false)
        listItemsRecyclerView = view.findViewById(R.id.list_items_reyclerview)
        listItemsRecyclerView.adapter = ListItemsRecyclerViewAdapter(list)
        listItemsRecyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    fun addTask(item: String) {
        list.tasks.add(item)
        val listRecyclerViewAdapter = listItemsRecyclerView.adapter as ListItemsRecyclerViewAdapter
        listRecyclerViewAdapter.list = list
        listRecyclerViewAdapter.notifyDataSetChanged()
    }

    companion object {
        fun newInstance(list: TaskList) : ListDetailFragment {
            val fragment = ListDetailFragment()
            val args = Bundle()
            args.putParcelable(ListActivity.INTENT_LIST_KEY, list)
            fragment.arguments = args

            return  fragment
        }
    }
}
