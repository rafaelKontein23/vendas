package br.com.visaogrupo.tudofarmarep.Presenter.View.Fragments.Cadastro

import FormularioCadastro
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.annotation.StringRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.ActSucessoCadastro
import br.com.visaogrupo.tudofarmarep.Presenter.View.Atividades.Cadastros.MainActivity
import br.com.visaogrupo.tudofarmarep.Presenter.View.Dialogs.Cadastro.DialogContrato
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.atividades.ViewModelActCabecalho
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.Factory.ViewModelFragmentContratoAceiteFactory
import br.com.visaogrupo.tudofarmarep.Presenter.ViewModel.Cadastro.fragments.ViewModelContratoAceite
import br.com.visaogrupo.tudofarmarep.R
import br.com.visaogrupo.tudofarmarep.Utils.Views.Alertas
import br.com.visaogrupo.tudofarmarep.Utils.Views.FormataTextos.Companion.aplicarMascaraCnpj
import br.com.visaogrupo.tudofarmarep.databinding.FragmentDadosContratoAceiteBinding


class DadosContratoAceiteFragment : Fragment() {
    var _binding : FragmentDadosContratoAceiteBinding? = null
    val binding get() = _binding!!
    private lateinit var  viewModelActCabecalho: ViewModelActCabecalho
    private lateinit var  viewModelContratoAceite: ViewModelContratoAceite



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding  = FragmentDadosContratoAceiteBinding.inflate(inflater, container, false)
        viewModelActCabecalho = ViewModelProvider(requireActivity()).get(ViewModelActCabecalho::class.java)
        viewModelActCabecalho.mudaProgressoCadastro(6, 1f)
        val factory = ViewModelFragmentContratoAceiteFactory(requireContext())
        viewModelContratoAceite = ViewModelProvider(this, factory)[ViewModelContratoAceite::class.java]


        if (FormularioCadastro.cadastro.isPoliticaPrivacidade ){
              mudarcheck(binding.aceiteBoxPoliticaPrivacidade)
        }
        if (FormularioCadastro.cadastro.isTermoPolitica){
            mudarcheck(binding.aceiteBoxTermosDeUso)
        }
        if (FormularioCadastro.cadastro.isAssinaContrato){
            mudarcheck(binding.assinaContratoBox)
        }

        viewModelContratoAceite.contratoAssinado.observe(viewLifecycleOwner){
            if (it){
                (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

                FormularioCadastro.cadastro.isAssinaContrato = true
                binding.assinaContratoBox.tag = "1"
                binding.assinaContratoBox.setBackgroundResource(R.drawable.bordas_radius_4_solid_blue600)

            }
            binding.scroolcontrato.post {
                binding.scroolcontrato.fullScroll(View.FOCUS_DOWN)
            }

        }

        binding.textoPolitica.setOnClickListener {
            val  dialog = DialogContrato(requireContext())
            dialog.dialogContratoPolitica(getString(R.string.politicaPrivacidade), getString(R.string.politica))

        }
        binding.textoTermosDeUso.setOnClickListener {
            val  dialog = DialogContrato(requireContext())
            dialog.dialogContratoPolitica(getString(R.string.termosDeUso), "${getString(R.string.temoDeuso)}  ${getString(R.string.segundaParteTermos)}")
        }
        binding.aceiteBoxPoliticaPrivacidade.setOnClickListener {
            mudarcheck(binding.aceiteBoxPoliticaPrivacidade)
            FormularioCadastro.cadastro.isPoliticaPrivacidade = binding.aceiteBoxPoliticaPrivacidade.tag == "1"
        }
        binding.aceiteBoxTermosDeUso.setOnClickListener {
            mudarcheck(binding.aceiteBoxTermosDeUso)
            FormularioCadastro.cadastro.isTermoPolitica = binding.aceiteBoxTermosDeUso.tag == "1"

        }
        binding.assinaContratoBox.setOnClickListener {
            (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val dialog = DialogContrato(requireContext())
            dialog.dialogAssina(viewModelContratoAceite)
        }
        binding.textoAssinar.setOnClickListener {
            (activity as Activity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            val dialog = DialogContrato(requireContext())
            dialog.dialogAssina(viewModelContratoAceite)

        }
        viewModelContratoAceite.fazCadastro.observe(viewLifecycleOwner){
            viewModelActCabecalho.mostraCarregando(false)
            if(it){
                 val intent = Intent(requireContext(), ActSucessoCadastro::class.java)
                 startActivity(intent)
                 requireActivity().finish()
            }else{
                Alertas.alertaErro(requireContext(), getString(R.string.erroCadastro), getString(R.string.tituloErro) ){
                }
            }
        }

        binding.btnContinuar.setOnClickListener {
            if (FormularioCadastro.cadastro.isPoliticaPrivacidade && FormularioCadastro.cadastro.isTermoPolitica && FormularioCadastro.cadastro.isAssinaContrato){
                  viewModelActCabecalho.mostraCarregando(true)
                  viewModelContratoAceite.enviaCadastroFinal()

            }else{
                validarAceites()
            }
        }

        binding.textoContrato.text = "TERMOS E CONDIÇÕES GERAIS DE USO DA PLATAFORMA LOIU\n" +
                "Este Termo e Condições Gerais (“Termo”) aplica-se ao uso dos serviços oferecidos pela LOIU SERVIÇOS DE TECNOLOGIA E\n" +
                "REPRESENTAÇÃO COMERCIAL LTDA, pessoa jurídica de direito privado, inscrita no CNPJ/MF sob o nº 47.423.823/0001-93, com sede na\n" +
                "Alameda Terracota, 215, Cerâmica, 5º andar, Cidade de São Caetano do Sul, Estado de São Paulo, para realização dos serviços objeto\n" +
                "deste Termo, doravante denominada LOIU.\n" +
                "${FormularioCadastro.cadastro.RazaoSocial}, pessoa jurídica de direito privado com sede em ${FormularioCadastro.cadastro.UF} - ${FormularioCadastro.cadastro.Cidade}, na ${FormularioCadastro.cadastro.Endereco}, ${FormularioCadastro.cadastro.CEP}, inscrita no CNPJ/ME sob o no.\n" +
                "${FormularioCadastro.cadastro.CNPJ.aplicarMascaraCnpj()}, neste ato por seu representante legal,${FormularioCadastro.cadastro.nome}, CPF ${FormularioCadastro.cadastro.cpf}, doravante denominada “USUÁRIO”\n" +
                "Os serviços disponíveis pela LOIU em sua plataforma serão regidos pelas cláusulas e condições abaixo.\n" +
                "Ao aceitar eletronicamente o presente Termo, através do clique no botão “Li e aceito os Termos de Uso” da página de cadastro, o\n" +
                "USUÁRIO estará automaticamente aderindo e concordando em se submeter integralmente a seus termos e condições e a qualquer de\n" +
                "suas alterações futuras, além de aceitar as disposições das Políticas de Privacidade\n" +
                "DEFINIÇÕES E OBJETO\n" +
                "Cláusula 1ª - DAS DEFINIÇÕES\n" +
                "1.1 Para exata compreensão e interpretação dos direitos e obrigações previstos no presente Termo, são adotadas as seguintes\n" +
                "definições:\n" +
                "CADASTRO INICIAL: ficha cadastral com os dados do USUÁRIO, informado ao iniciar o cadastro na PLATAFORMA LOIU.\n" +
                "CADASTRO COMPLETO: ficha cadastral recebida pelo USUÁRIO, após preenchimento do cadastro inicial e cumprimento das exigências\n" +
                "necessárias para acessar às funcionalidades da PLATAFORMA LOIU, cujo preenchimento é necessário para que o cadastro seja\n" +
                "analisado e, se aprovado, o USUÁRIO poderá acessar a plataforma de gestão e todas as suas funcionalidades.\n" +
                "FABRICANTE: fabricantes dos produtos disponibilizados para venda pelo USUÁRIO;\n" +
                "USUÁRIO: usuário da PLATAFORMA LOIU que, na qualidade de profissional autônomo, poderá agenciar propostas e pedidos\n" +
                "correspondentes aos produtos disponibilizados pelo FABRICANTE, além de realizar outras ações, sendo que tal atividade poderá ser\n" +
                "exercida em caráter permanente ou temporário, para uma ou mais empresas ou marcas.\n" +
                "CLIENTE: estabelecimento que irá adquirir os produtos ofertados pelo FABRICANTE, ou que receberá outras ações ofertadas pela\n" +
                "PLATAFORMA LOIU, por meio das negociações realizadas pelo USUÁRIO e formalizadas através das ferramentas específicas para\n" +
                "operação de vendas e demais ações, disponibilizadas pela PLATAFORMA LOIU.\n" +
                "PLATAFORMA LOIU: consiste em uma plataforma digital através da qual os USUÁRIOS terão acesso à diversas oportunidades junto a\n" +
                "dezenas de MARCAS PARCEIRAS para realizar ações disponibilizadas na PLATAFORMA LOIU. O serviço da LOIU é limitado à gestão da\n" +
                "aplicação (PLATAFORMA) e demais serviços associados, como gerência de pedidos e pagamentos, colaboração na resolução de\n" +
                "problemas nos pedidos dentre outros.\n" +
                "APLICATIVO: programa que deverá ser obtido através de download junto do serviço de distribuição digital de aplicativos compatível com\n" +
                "o sistema operacional do aparelho celular do USUÁRIO, por meio do qual o USUÁRIO poderá solicitar a disponibilização dos Serviços da\n" +
                "PLATAFORMA LOIU, mediante preenchimento dos CADASTROS necessários e posterior aceite do presente termo e aprovação do perfil\n" +
                "comercial do USUÁRIO pela PLATAFORMA LOIU.\n" +
                "AÇÕES: Todas as atividades que podem ser realizadas pelo USUÁRIO em relação a cada tipo de produto ou MARCAS PARCEIRAS, tais\n" +
                "como, pesquisas no CNPJ, intermediação de venda, merchandising, consultoria, positivação, dentre outros.\n" +
                "Cláusula 2ª – DO OBJETO\n" +
                "2.1 O presente instrumento tem por objeto estabelecer as regras para a prestação dos serviços de tecnologia pela LOIU ao USUÁRIO,\n" +
                "bem como os termos e condições para uso das ferramentas disponibilizadas na PLATAFORMA LOIU, pelo USUÁRIO.\n" +
                "(atualizado em 09 de setembro de 2022)\n" +
                "2.2 O serviço da LOIU é limitado à disponibilização da aplicação (PLATAFORMA) e demais serviços associados à tecnologia, como\n" +
                "desenvolvimento de software, gestão de servidores, e sistemas tecnológicos dentre outros.\n" +
                "2.3 LOIU não é responsável pelo conteúdo e funcionamento da operação de venda, condições de faturamento, estoque, aprovação de\n" +
                "crédito, entrega de produtos ou quaisquer condições disponibilizadas pelas MARCAS PARCEIRAS, nem mesmo por danos e/ou\n" +
                "prejuízos resultantes destas operações.\n" +
                "2.4 Este termo não regula a relação entre o USUÁRIO e o FABRICANTE haja vista que a PLATAFORMA LOIU não controla ou influencia\n" +
                "e, portanto, não é responsável, direta ou indiretamente, por quaisquer atividades que envolvam estas relações.\n" +
                "DAS OPERAÇÕES DO USUÁRIO NA PLATAFORMA LOIU\n" +
                "Cláusula 3ª - DA CRIAÇÃO DE CONTA E USO DA PLATAFORMA\n" +
                "3.1 Para utilização da PLATAFORMA LOIU, o USUÁRIO deverá preencher o CADASTRO COMPLETO disponibilizado após o envio do\n" +
                "CADASTRO INICIAL, para a obtenção de um TOKEN de acesso à PLATAFORMA LOIU, informando todos os dados exigidos.\n" +
                "3.2 A PLATAFORMA LOIU ficará à disposição do USUÁRIO 24 (vinte e quatro) horas por dia, 7 (sete) dias por semana, com exceção de\n" +
                "manutenções regulares ou problemas técnicos.\n" +
                "3.3 O USUÁRIO compromete-se a notificar a LOIU imediatamente, a respeito de qualquer uso não autorizado de sua conta, bem como\n" +
                "o acesso não autorizado por terceiros, haja vista que o USUÁRIO será o único responsável pelas operações efetuadas em sua conta.\n" +
                "3.4 A PLATAFORMA LOIU se reserva o direito de utilizar todos os meios válidos e possíveis para identificar os USUÁRIOS, portanto\n" +
                "poderá a LOIU, a qualquer momento, à seu critério, solicitar cópias de documentos do USUÁRIO, de forma a garantir a veracidade dos\n" +
                "dados. Nestes casos, a LOIU poderá suspender o fornecimento de serviços até o recebimento dos documentos solicitados, ficando\n" +
                "isenta de qualquer responsabilidade ou ressarcimento ao USUÁRIO.\n" +
                "3.4.1 Caso algum dado seja verificado pela PLATAFORMA LOIU como, o USUÁRIO deverá efetuar as devidas correções, sob pena\n" +
                "de ter o seu acesso à PLATAFORMA LOIU bloqueado, até a regularização do CADASTRO, podendo inclusive cancelar\n" +
                "definitivamente o referido CADASTRO, se assim entender necessário à proteção dos interesses da PLATAFORMA LOIU e seus\n" +
                "usuários.\n" +
                "3.5 O nome completo que o USUÁRIO utiliza na PLATAFORMA LOIU não poderá guardar semelhança com o nome LOIU, tampouco\n" +
                "poderá ser utilizado qualquer nome que insinue ou sugira que os dados ali cadastrados pertencem à LOIU. Também serão eliminados\n" +
                "nomes considerados ofensivos, bem como os que contenham outros dados pessoais do USUÁRIO ou alguma URL ou endereço\n" +
                "eletrônico.\n" +
                "3.6 O USUÁRIO será integralmente responsável por certificar-se de que a configuração de seu equipamento está em pleno acordo com\n" +
                "os requisitos mínimos de segurança, necessários à fruição dos serviços oferecidos pela LOIU, que estará livre e isenta de qualquer\n" +
                "responsabilidade decorrente da não observância do disposto nesta cláusula.\n" +
                "3.7 É ressalvado à LOIU o direito de suspender ou excluir definitivamente da plataforma o USUÁRIO que descumprir os termos desse\n" +
                "instrumento, o Código de Conduta da LOIU ou qualquer política ou documento da LOIU que contenha obrigações do USUÁRIO.\n" +
                "3.8 As atividades de mediação mercantil não possuem caráter de exclusividade, sendo que o USUÁRIO pode desempenhar\n" +
                "negociações com outras empresas, ainda que concorram com os FABRICANTES.\n" +
                "Cláusula 4ª - DOS PRODUTOS E LOCAIS DISPONÍVEIS PARA AÇÕES DO USUÁRIO\n" +
                "4.1 Os produtos e ações objeto das negociações serão especificados e exibidos no sistema do USUÁRIO, conforme disponibilização\n" +
                "pelo FABRICANTE, de maneira dinâmica, ou seja, podem sofrer alterações, cabendo ao USUÁRIO acompanhar, constantemente, as\n" +
                "informações disponibilizadas na PLATAFORMA. As condições informadas no momento da conclusão do pedido ou ação, contudo, não\n" +
                "poderão ser objeto de alteração.\n" +
                "4.2 O USUÁRIO poderá desempenhar a sua atividade nos CLIENTES autorizados pelo FABRICANTE para cada produto ou ação\n" +
                "disponível, dentre as regiões geográficas selecionadas pelo USUÁRIO na plataforma.\n" +
                "4.2.1 Os locais autorizados para as ações do USUÁRIO também serão exibidos na PLATAFORMA LOIU, de maneira dinâmica e\n" +
                "sujeitos a alteração conforme as condições momentâneas estabelecidas livremente pela PLATAFORMA.\n" +
                "4.3 O USUÁRIO não contará com exclusividade para realizar a negociação de quaisquer produtos ou para atuar em quaisquer regiões\n" +
                "geográficas, salvo nos casos especificados neste instrumento ou em outros documentos oficiais disponibilizados pela PLATAFORMA\n" +
                "LOIU ou pelo FABRICANTE como, por exemplo, termos específicos versando acerca da exclusividade.\n" +
                "4.4 Sem prejuízo do quanto disposto na cláusula 4.3 acima, o USUÁRIO poderá obter autorização para atuar, de maneira exclusiva,\n" +
                "temporariamente em regiões ou CLIENTES específicos para negociação de produtos e ações indicados previamente na PLATAFORMA,\n" +
                "conforme os parâmetros definidos livremente e de maneira variável pelos FABRICANTES e/ou pela PLATAFORMA LOIU.\n" +
                "4.4.1 As informações sobre eventuais autorizações para atuação exclusiva serão exibidas na PLATAFORMA do USUÁRIO, de\n" +
                "maneira dinâmica e variável.\n" +
                "4.4.2 A exclusividade mencionada nesta cláusula 4.4 poderá ser revogada a qualquer tempo, hipótese que será devidamente\n" +
                "sinalizada no portal do USUÁRIO, respeitando-se as condições estabelecidas no momento da concessão da exclusividade.\n" +
                "CLÁUSULA 5ª - DOS PAGAMENTOS\n" +
                "5.1 Durante a vigência deste Termo, a LOIU compromete-se a pagar o percentual ou valor atribuído às ações realizadas pelo USUÁRIO.\n" +
                "5.2 Os percentuais da comissão de vendas e pagamento sobre ações que o USUÁRIO terá direito serão exibidos na PLATAFORMA no\n" +
                "momento exato da venda ou ação, podendo variar de acordo com o produto, valores, quantidade ou outros critérios definidos\n" +
                "livremente pelo FABRICANTE.\n" +
                "5.3 Os pagamentos das operações comerciais decorrentes das negociações, serão gerenciados pela LOIU que fará o devido\n" +
                "pagamento da comissão e/ou outros valores correspondentes às negociações realizadas pelo USUÁRIO, em até 60 (sessenta) dias\n" +
                "contados a partir do mês subsequente à efetivação das negociações.\n" +
                "5.3.1 Caso o pedido ou ação não se consolide em razão da falta do item, problemas de logística, problemas de crédito, devolução\n" +
                "ou na hipótese de não aprovação da ação, o USUÁRIO não fará jus ao recebimento de qualquer comissão ou pagamento.\n" +
                "5.3.2 A LOIU poderá antecipar o pagamento das comissões ao USUÁRIO, sendo que na hipótese de não consolidação da a\n" +
                "valor do adiantamento será deduzido das próximas comissões que lhe sejam de\n" +
                "5.4 Ao concluir cada transação o USUÁRIO atesta que concorda com as condições comerciais definidas entre as partes no momento\n" +
                "do fechamento do pedido.\n" +
                "5.5 Após a conclusão do pedido ou da ação o valor será mantido.\n" +
                "5.6 No momento da transmissão dos pedidos, serão exibidos, no painel do USUÁRIO, os detalhes correspondentes às vendas\n" +
                "negociadas ou às ações realizadas e aos valores provisionados em favor do USUÁRIO.\n" +
                "5.6.1 Ao concluir uma ação, seja de vendas, merchandising ou comercial, o USUÁRIO deverá declarar que concorda com as\n" +
                "condições relacionadas àquela ação, clicando no ícone “aprovar condições” no prazo de 24h (vinte e quatro horas) contados a\n" +
                "partir da conclusão do pedido, sob pena de desconsideração total da ação pretendida e cancelamento.\n" +
                "5.6.2 Caso o USUÁRIO discorde das condições exibidas no espelho da ação, poderá apresentar a sua reivindicação clicando no\n" +
                "ícone “cancelar e informar divergência” hipótese em que a ação será descontinuada e cancelada.\n" +
                "5.7 O percentual de pagamento das comissões e o valor atribuído à outras ações serão informadas na PLATAFORMA e poderão variar\n" +
                "de acordo com as condições estabelecidas pela PLATAFORMA LOIU e exibidas no momento da escolha dos produtos ou ações para\n" +
                "negociação.\n" +
                "5.8 O pagamento somente será realizado mediante apresentação da respectiva nota fiscal, com antecedência mínima de\n" +
                "dias. Referida nota fiscal deve ser emitida exclusivamente em nome do USUÁRIO, sendo vedado o envio de Notas Ficais emitidas em\n" +
                "nome de terceiros.\n" +
                "5.9 O descumprimento das condições definidas neste instrumento acarretará na prorrogação do prazo de pagamento indicado no item\n" +
                "5.3 acima, que será contado a partir da correção dos atos irregulares.\n" +
                "5.10 O pagamento das quantias disponíveis ao USUÁRIO estará sempre condicionado ao envio da nota fiscal pelo USUÁRIO através da\n" +
                "plataforma ou do e-mail nota@loiu.com.br bem como ao cadastro e/ou validação do CLIENTE, na forma do item 5.6.3 acima.\n" +
                "5.11 Através da PLATAFORMA LOIU, o USUÁRIO poderá solicitar o pagamento através da instituição financeira de sua escolha, e o\n" +
                "valor será transferido exclusiva e diretamente para a conta bancária do USUÁRIO ou da Pessoa Física responsável, sendo esta última\n" +
                "hipótese permitida somente para USUÁRIO MEI. Não serão realizados pagamentos para contas bancárias cadastradas em nome de\n" +
                "terceiros.\n" +
                "5.12 Qualquer alteração de cadastro bancário do USUÁRIO deverá ser devidamente objeto de atualização na PLATAFORMA LOIU, com\n" +
                "no mínimo 30 (trinta) dias de antecedência ao vencimento das próximas faturas, sendo que a impossibilidade ou atraso de pagamento\n" +
                "ao USUÁRIO por inconsistência nas informações cadastradas junto à plataforma não poderão ser consideradas como hipóteses de\n" +
                "inadimplemento por parte da LOIU.\n" +
                "5.13 O USUÁRIO será responsável por custear quaisquer despesas acessórias relacionadas à atividade exercida, tais como gastos\n" +
                "com transporte, alimentação, dentre outros, ressalvada a hipótese de negociação celebrada entre o USUÁRIO e a PLATAFORMA LOIU\n" +
                "em sentido diverso.\n" +
                "OBRIGAÇÕES E RESPONSABILIDADES\n" +
                "Cláusula 6ª – DAS OBRIGAÇÕES DO USUÁRIO\n" +
                "6.1 Sem prejuízo das demais obrigações estabelecidas neste instrumento, o USUÁRIO se obriga realizar a negociação e vendas ao\n" +
                "CLIENTE e a cumprir todas as obrigações legais, administrativas, contratuais, dentre outras relacionadas, devendo observar todas as\n" +
                "exigências legais para exercício das mediações de venda.\n" +
                "6.2 O USUÁRIO declara e garante que sempre divulgará e disponibilizará aos clientes finais informação de quantidade, preço, validade,\n" +
                "fotos, modalidade de entrega, prazos de entrega, política de devolução e troca e demais dados necessários para a utilização segura e\n" +
                "eficaz dos produtos de acordo com o quanto previsto nas na legislação e em consonância com as informações prestadas pelo\n" +
                "FABRICANTE.\n" +
                "6.3 O USUÁRIO assume desde já responsabilidade civil e criminal pela emissão de pedidos ou ações não autorizadas pelo CLIENTE\n" +
                "e/ou em desacordo com a legislação vigente, ou por qualquer ato omisso, e arcará com todas as despesas que a LOIU venha a\n" +
                "suportar.\n" +
                "6.4 Para efetivar uma ação junto ao CLIENTE, o USUÁRIO deverá cadastrar, junto à PLATAFORMA LOIU, um responsável do CLIENTE\n" +
                "para acompanhamento e/ou validação da ação através de link a ser encaminhado automaticamente pela PLATAFORMA LOIU,\n" +
                "observando-se o quanto disposto no item 6.5 (iii).\n" +
                "6.5 O USUÁRIO será exclusivamente responsável:\n" +
                "(i) Por cumprir, com todas as leis e normas vigentes, bem como providenciar a obtenção das licenças, alvarás e autorizações\n" +
                "necessárias para exercício das atividades de mediação mercantil, sendo responsável por perdas e danos diretos causados à LOIU\n" +
                "ou ao FABRICANTE, decorrentes de infrações a que o USUÁRIO venham a dar causa;\n" +
                "(ii) Por assumir toda e qualquer responsabilidade a que der causa, sobre qualquer reclamação cível, tributária, trabalhista\n" +
                "previdenciária e/ou criminal, multas, perdas e danos e despesas, inclusive honorários advocatícios, decorrentes da violação de\n" +
                "qualquer uma das condições deste instrumento ou provenientes do descumprimento de leis;\n" +
                "(iii) Por garantir e responder pela veracidade das informações preenchidas ou compartilhadas através da PLATAFORMA LOIU,\n" +
                "inclusive perante terceiros, declarando estar ciente que o cadastro de informações falsas e/ou fraudulentas corresponde a crime\n" +
                "previsto pela lei nº 12.965 e no código penal;\n" +
                "(iv) Por cumprir o código de conduta desenvolvido pela LOIU e eventuais requisitos impostos pelos FABRICANTES com quem\n" +
                "contratar;\n" +
                "(v) pelo pagamento e o recolhimento de todos os tributos e encargos, que sejam devidos em razão da realização do presente\n" +
                "contrato.\n" +
                "6.6 A LOIU se reserva o direito de suspender temporariamente, bloquear ou descadastrar\n" +
                "${FormularioCadastro.cadastro.nome}, ${FormularioCadastro.cadastro.cpf}\n" +
                "${FormularioCadastro.cadastro.CNPJ.aplicarMascaraCnpj()}, ${FormularioCadastro.cadastro.RazaoSocial}"

        return  binding.root
    }

    fun mudarcheck( constraintLayout: ConstraintLayout){
        if (constraintLayout.tag == "1"){
            constraintLayout.tag = "0"
            constraintLayout.setBackgroundResource(R.drawable.bordas_radius_4dp_strokecolor_gray400_solid_gray100)
        }else{
            constraintLayout.tag = "1"
            constraintLayout.setBackgroundResource(R.drawable.bordas_radius_4_solid_blue600)
        }
    }
    private fun validarAceites() {
        val cadastro = FormularioCadastro.cadastro

        when {
            // Nenhum aceite foi selecionado
            !cadastro.isPoliticaPrivacidade && !cadastro.isTermoPolitica && !cadastro.isAssinaContrato -> {
                exibirErro(R.string.erroAceites)
            }

            // Política de privacidade e contrato aceitos, mas termo não
            cadastro.isPoliticaPrivacidade && cadastro.isAssinaContrato && !cadastro.isTermoPolitica -> {
                exibirErro(R.string.erroTermo)
            }

            // Termo e política de privacidade aceitos, mas contrato não
            cadastro.isTermoPolitica && cadastro.isPoliticaPrivacidade && !cadastro.isAssinaContrato -> {
                exibirErro(R.string.erroContrato)
            }

            // Apenas contrato aceito
            cadastro.isAssinaContrato && !cadastro.isTermoPolitica && !cadastro.isPoliticaPrivacidade -> {
                exibirErro(R.string.erroTermosEPolitica)
            }

            // Contrato e política aceitos, mas termo não
            cadastro.isAssinaContrato && !cadastro.isTermoPolitica && cadastro.isPoliticaPrivacidade -> {
                exibirErro(R.string.erroTermosEContrato)
            }

            // Contrato e termo aceitos, mas política não
            cadastro.isAssinaContrato && cadastro.isTermoPolitica && !cadastro.isPoliticaPrivacidade -> {
                exibirErro(R.string.erroContratoETermos)
            }

            // Termo aceito, mas política e contrato não
            !cadastro.isAssinaContrato && cadastro.isTermoPolitica && !cadastro.isPoliticaPrivacidade -> {
                exibirErro(R.string.erroContratoEPolitica)
            }

            // Política aceita, mas termo e contrato não
            !cadastro.isAssinaContrato && !cadastro.isTermoPolitica && cadastro.isPoliticaPrivacidade -> {
                exibirErro(R.string.erroTermosEContrato)
            }
        }
    }

    private fun exibirErro(@StringRes mensagemErro: Int) {
        Alertas.alertaErro(
            context = requireContext(),
            mensagem = getString(mensagemErro),
            titulo = getString(R.string.tituloErro)
        ) {}
    }

}