package com.example.com.example.lego

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.lego.R
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.QuerySnapshot

class MyAdapter(private val context: Context, private val data: MutableList<String>) : BaseAdapter()  {
    override fun getCount(): Int = data.size

    override fun getItem(position: Int): Any = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder
        val view: View

        if (convertView == null){
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder as ViewHolder
        } else {
            view = convertView!!
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.textView.text = data[position]
        return view
    }
    inner class ViewHolder(view: View){
        val textView: TextView = view.findViewById(R.id.textView)
    }
}