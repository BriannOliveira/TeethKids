package br.com.minhaempresa.teethkids.menu.reputation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.minhaempresa.teethkids.databinding.FragmentReputationBinding
import br.com.minhaempresa.teethkids.menu.emergency.recyclerViewEmergencies.EmergencyAdapter
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.Avaliation
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.AvaliationAdapter
import br.com.minhaempresa.teethkids.menu.reputation.recyclerViewReputation.avaliationslist
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel


class ReputationFragment : Fragment(), EmergencyAdapter.RecyclerViewEvent {

    private var _binding : FragmentReputationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentReputationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buscarDados()

        //configurar o recyclerView
        var recyclerView = binding.rvAvaliations
        var avaliationAdapter = AvaliationAdapter(avaliationslist,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = avaliationAdapter

        //configurando cardview estilizado
        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setBottomLeftCorner(CornerFamily.ROUNDED, 0f)
            .setBottomRightCorner(CornerFamily.ROUNDED, 0f)
            .setTopLeftCorner(CornerFamily.ROUNDED,80f)
            .setTopRightCorner(CornerFamily.ROUNDED,80f)
            .build()

        binding.cvAvaliations.shapeAppearanceModel = shapeAppearanceModel

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buscarDados(){



    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

}