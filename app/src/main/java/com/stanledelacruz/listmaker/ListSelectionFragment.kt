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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListSelectionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListSelectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListSelectionFragment : Fragment(), ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {

    lateinit var listsRecyclerView: RecyclerView
    lateinit var listDataManager: ListDataManager

    // 1
    private var listener: OnListItemFragmentInteractionListener? = null
    interface  OnListItemFragmentInteractionListener {
        fun onListItemClicked(list: TaskList)
    }

    // 2
    companion object {
        fun newInstance(): ListSelectionFragment {
            return  ListSelectionFragment()
        }
    }

    // 3
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context)
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    // 4
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupDataManager()
    }

    // 5
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_selection, container, false)
    }

    // 6
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun setupDataManager() {
        val lists = listDataManager.readList()

        view?.let {
            listsRecyclerView = it.findViewById(R.id.list_recyclerview)
            listsRecyclerView.layoutManager = LinearLayoutManager(activity)
            listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
        }
    }

    fun addList(list: TaskList) {
        listDataManager.saveList(list)

        val recyclerViewAdapter = listsRecyclerView.adapter as ListSelectionRecyclerViewAdapter
        recyclerViewAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readList()
        listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists, this)
    }

    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClicked(list)
    }
}
