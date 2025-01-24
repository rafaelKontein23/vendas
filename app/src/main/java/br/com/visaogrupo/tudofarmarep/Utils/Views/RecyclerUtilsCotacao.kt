package br.com.visaogrupo.tudofarmarep.Utils.Views

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import br.com.visaogrupo.tudofarmarep.Adapter.AdapterCotacao
import br.com.visaogrupo.tudofarmarep.Carga.Tasks.TasksHome.TaskCotacao
import br.com.visaogrupo.tudofarmarep.DAO.DAOCarrinho
import br.com.visaogrupo.tudofarmarep.DAO.DAOHelper
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Repository.Model.Cadastro.Requisicao.ExcluiCotacaoRequest
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerUtilsCotacao(val adpter: AdapterCotacao) {

    fun item(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // Não é necessário implementar o movimento para reordenar
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Aqui você pode remover o item da lista
                // Exemplo: yourAdapter.removeItem(position)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val maxSwipeDistance =
                    itemView.width * 0.3f
                val limitedDx = if (dX < -maxSwipeDistance) -maxSwipeDistance else dX

                if (dX > 0) {
                    c.drawColor(Color.TRANSPARENT) // Limpa a tela para a área normal
                }

                if (dX < 0) { // Apenas desenha se a célula estiver sendo arrastada para a esquerda
                    val backgroundPaint = Paint().apply { color = Color.RED }
                    c.drawRect(
                        itemView.right.toFloat() + limitedDx,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat(),
                        backgroundPaint
                    )

                    // Desenha o ícone da lixeira
                    val deleteIcon =
                        ContextCompat.getDrawable(recyclerView.context, R.drawable.lixeira)
                    deleteIcon?.let {
                        val iconMargin = (itemView.height - it.intrinsicHeight) / 2
                        val iconTop = itemView.top + iconMargin
                        val iconBottom = iconTop + it.intrinsicHeight
                        val iconLeft = itemView.right - iconMargin - it.intrinsicWidth
                        val iconRight = itemView.right - iconMargin

                        it.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                        it.draw(c)
                    }
                }

                recyclerView.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_UP) {
                        val touchX = event.x
                        val touchY = event.y

                        // Defina os limites da área vermelha
                        val redAreaLeft = itemView.right.toFloat() + limitedDx
                        val redAreaRight = itemView.right.toFloat()
                        val redAreaTop = itemView.top.toFloat()
                        val redAreaBottom = itemView.bottom.toFloat()

                        if (touchX in redAreaLeft..redAreaRight && touchY in redAreaTop..redAreaBottom) {
                            onRedAreaClicked(viewHolder.adapterPosition, recyclerView) // Ação ao clicar na área vermelha
                            return@setOnTouchListener true
                        }
                    }
                    false
                }

                // Reseta o estado quando o swipe termina
                if (!isCurrentlyActive && dX == 0f) {
                    recyclerView.invalidate() // Redesenha a RecyclerView
                }

                // Continua o swipe dentro dos limites
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    limitedDx,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
    }
    private fun onRedAreaClicked(position: Int, recyclerView: RecyclerView) {

        val itemRecy = adpter.listaCotacao[position]
        adpter.listaCotacao.removeAt(position)
        adpter.notifyItemRemoved(position)


        val snackbar = Snackbar.make(
            recyclerView,
            "Item excluído",
            Snackbar.LENGTH_LONG
        ).setBackgroundTint(Color.WHITE)
            .setTextColor(Color.BLACK)
            .setAction("Desfazer") {
                adpter.listaCotacao.add(position,itemRecy)
                adpter.notifyItemInserted(position)
            }
            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (event != DISMISS_EVENT_ACTION) {
                        CoroutineScope(Dispatchers.IO).launch{
                            val apagaCotacao = TaskCotacao()
                            val excluiCotacaoRequest = ExcluiCotacaoRequest(itemRecy.CarrinhoId)
                            apagaCotacao.excluiCotacao(excluiCotacaoRequest)

                        }

                    }
                }
            })

        snackbar.show()
    }

}