package br.com.visaogrupo.tudofarmarep.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class AdapterFragmentItens(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentList: MutableList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
    fun clearFragments() {
        fragmentList.clear()
        notifyDataSetChanged() // Notifica que os dados foram alterados
    }

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }
}