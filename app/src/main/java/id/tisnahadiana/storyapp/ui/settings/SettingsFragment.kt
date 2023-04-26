package id.tisnahadiana.storyapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import id.tisnahadiana.storyapp.R
import id.tisnahadiana.storyapp.databinding.FragmentSettingsBinding
import id.tisnahadiana.storyapp.ui.login.LoginActivity
@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
    }

    private fun setView() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings_title)
        binding?.apply {
            btnLanguage.setOnClickListener { settingLanguage() }
            btnLogout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle(R.string.logout)
        alertDialogBuilder.setMessage(R.string.logout_confirmation)
        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            run {
                settingsViewModel.logout()
                Intent(requireContext(), LoginActivity::class.java).also { intent ->
                    startActivity(intent)
                    requireActivity().finish()
                }

            }
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    private fun settingLanguage() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}