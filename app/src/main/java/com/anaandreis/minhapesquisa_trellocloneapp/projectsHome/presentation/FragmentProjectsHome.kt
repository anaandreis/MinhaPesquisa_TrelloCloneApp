    package com.anaandreis.minhapesquisa_trellocloneapp.projectsHome.presentation

    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.databinding.DataBindingUtil
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.viewModels
    import androidx.lifecycle.Observer
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.anaandreis.minhapesquisa_trellocloneapp.R
    import com.anaandreis.minhapesquisa_trellocloneapp.databinding.FragmentProjectsHomeBinding
    import com.anaandreis.minhapesquisa_trellocloneapp.newProject.NewProjectFragment
    import com.anaandreis.minhapesquisa_trellocloneapp.newProject.NewProjectViewModel
    import com.anaandreis.minhapesquisa_trellocloneapp.newProject.Project
    import com.anaandreis.minhapesquisa_trellocloneapp.newProject.adapterProjects.projectsAdapter


    class FragmentProjectsHome : Fragment() {

        private lateinit var binding: FragmentProjectsHomeBinding
        private lateinit var recyclerView: RecyclerView
        private val sharedViewModel: NewProjectViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_projects_home, container, false
            )


            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.apply {
                //         // Specify the fragment as the lifecycle owner

                lifecycleOwner = viewLifecycleOwner

                // Assign the view model to a property in the binding class
                homeProjectViewModel = sharedViewModel

                // Assign the fragment
                homeProjectFragment = this@FragmentProjectsHome

            }
            sharedViewModel.getBoardsList()

            sharedViewModel.projectsList.observe(viewLifecycleOwner, Observer { projectsList ->
                if (projectsList.isNotEmpty()) {
                    populateProjectsRecyclerView(projectsList as ArrayList<Project>)
                }
            })
            binding.addProjectButton.setOnClickListener { showCreateProjectSheet() }

        }

        override fun onDestroyView() {
            super.onDestroyView()
            removeProjectsListener()
        }

        fun showCreateProjectSheet() {
            val modalBottomSheet = NewProjectFragment()
            modalBottomSheet.show(parentFragmentManager, "ProjectSheet")
        }

       fun populateProjectsRecyclerView(ProjectsList: ArrayList<Project>) {

           binding.projectListRecyclerView.visibility = View.VISIBLE
           recyclerView = view!!.findViewById(R.id.projectListRecyclerView)
           recyclerView.layoutManager = LinearLayoutManager(requireContext())
           val adapter = projectsAdapter(requireContext(), ArrayList())
           adapter.clear() // clear the adapter's list before adding new items
           adapter.addAll(ProjectsList) // add new items to the adapter's list
           recyclerView.adapter = adapter


       }

        private fun removeProjectsListener() {
            sharedViewModel.projectsListenerRegistration?.remove()
        }
    }