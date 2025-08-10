import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  TextField,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Alert,
  CircularProgress,
  Grid,
  Divider,
  Snackbar,
  Chip,
  Fade
} from '@mui/material';
import {
  Add as AddIcon,
  Delete as DeleteIcon,
  ShoppingCart as CartIcon,
  CheckCircle as CheckCircleIcon,
  Error as ErrorIcon,
  Info as InfoIcon,
  Warning as WarningIcon,
  Wifi as WifiIcon,
  WifiOff as WifiOffIcon,
  Refresh as RefreshIcon
} from '@mui/icons-material';
import { customerService, productService, orderService } from '../api/services';

const PedidoForm = () => {
  // State for form data
  const [clientes, setClientes] = useState([]);
  const [produtos, setProdutos] = useState([]);
  const [selectedCliente, setSelectedCliente] = useState('');
  const [selectedProduto, setSelectedProduto] = useState('');
  const [quantidade, setQuantidade] = useState(1);
  const [itensPedido, setItensPedido] = useState([]);
  const [selectedClienteDetails, setSelectedClienteDetails] = useState(null);
  
  // State for real-time credit balance
  const [creditUsage, setCreditUsage] = useState({
    valorUtilizado: 0,
    saldoDisponivel: 0,
    loading: false
  });
  
  // State for UI control
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [retryLoading, setRetryLoading] = useState(false);
  const [addingProduct, setAddingProduct] = useState(false);
  const [networkError, setNetworkError] = useState(false);
  const [isOnline, setIsOnline] = useState(navigator.onLine);
  const [message, setMessage] = useState({ type: '', text: '' });
  
  // Snackbar state for enhanced feedback
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success', // success, error, warning, info
    duration: 6000
  });

  // Helper function to show snackbar notifications
  const showSnackbar = (message, severity = 'success', duration = 6000) => {
    setSnackbar({
      open: true,
      message,
      severity,
      duration
    });
  };

  // Helper function to close snackbar
  const handleCloseSnackbar = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  // Load customers and products on component mount
  useEffect(() => {
    const loadData = async () => {
      try {
        setLoading(true);
        setNetworkError(false);
        const [clientesResponse, produtosResponse] = await Promise.all([
          customerService.getAll(),
          productService.getAll()
        ]);
        
        setClientes(clientesResponse.data.content || clientesResponse.data || []);
        setProdutos(produtosResponse.data.content || produtosResponse.data || []);
        setMessage({ type: 'success', text: 'Dados carregados com sucesso!' });
        showSnackbar('Dados de clientes e produtos carregados com sucesso!', 'success', 4000);
      } catch (error) {
        console.error('Error loading data:', error);
        setNetworkError(true);
        
        let errorMessage = 'Erro ao carregar dados: ';
        
        if (!navigator.onLine) {
          errorMessage += 'Sem conexão com a internet. Verifique sua conexão e tente novamente.';
        } else if (error.code === 'ERR_NETWORK') {
          errorMessage += 'Falha na conexão com o servidor. O backend pode estar indisponível.';
        } else if (error.response?.status === 500) {
          errorMessage += 'Erro interno do servidor. Tente novamente em alguns minutos.';
        } else if (error.response?.status === 404) {
          errorMessage += 'Serviço não encontrado. Verifique se o backend está rodando.';
        } else if (error.response?.status >= 400 && error.response?.status < 500) {
          errorMessage += `Erro no cliente (${error.response.status}): ${error.response.statusText}`;
        } else {
          errorMessage += error.message || 'Erro desconhecido. Tente recarregar a página.';
        }
        
        setMessage({ type: 'error', text: errorMessage });
        showSnackbar(errorMessage, 'error', 10000);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  // Retry data loading function
  const handleRetryLoading = async () => {
    try {
      setRetryLoading(true);
      setNetworkError(false);
      setMessage({ type: '', text: '' });
      
      const [clientesResponse, produtosResponse] = await Promise.all([
        customerService.getAll(),
        productService.getAll()
      ]);
      
      setClientes(clientesResponse.data.content || clientesResponse.data || []);
      setProdutos(produtosResponse.data.content || produtosResponse.data || []);
      setMessage({ type: 'success', text: 'Dados recarregados com sucesso!' });
      showSnackbar('Conexão restaurada! Dados carregados com sucesso.', 'success', 4000);
    } catch (error) {
      console.error('Retry error:', error);
      let errorMessage = 'Falha ao reconectar: ';
      
      if (!navigator.onLine) {
        errorMessage += 'Ainda sem conexão com a internet.';
      } else if (error.code === 'ERR_NETWORK') {
        errorMessage += 'Servidor ainda indisponível.';
      } else {
        errorMessage += error.message || 'Erro desconhecido.';
      }
      
      setMessage({ type: 'error', text: errorMessage });
      showSnackbar(errorMessage, 'error', 8000);
    } finally {
      setRetryLoading(false);
    }
  };

  // Network status monitoring
  useEffect(() => {
    const handleOnline = () => {
      setIsOnline(true);
      setNetworkError(false);
      showSnackbar('Conexão restaurada!', 'success', 3000);
    };
    
    const handleOffline = () => {
      setIsOnline(false);
      setNetworkError(true);
      showSnackbar('Conexão perdida. Verificando...', 'warning', 5000);
    };

    window.addEventListener('online', handleOnline);
    window.addEventListener('offline', handleOffline);

    return () => {
      window.removeEventListener('online', handleOnline);
      window.removeEventListener('offline', handleOffline);
    };
  }, []);

  // Function to fetch real-time credit usage for selected customer
  const fetchCustomerCreditUsage = async (clienteId) => {
    try {
      setCreditUsage(prev => ({ ...prev, loading: true }));
      
      const response = await customerService.getCreditBalance(clienteId);
      const creditInfo = response.data;
      
      setCreditUsage({
        valorUtilizado: parseFloat(creditInfo.valorUtilizado || 0),
        saldoDisponivel: parseFloat(creditInfo.saldoDisponivel || 0),
        loading: false
      });
      
      setSelectedClienteDetails(prev => ({
        ...prev,
        valorUtilizado: parseFloat(creditInfo.valorUtilizado || 0),
        saldoDisponivel: parseFloat(creditInfo.saldoDisponivel || 0)
      }));
      
    } catch (error) {
      console.error('Error fetching credit usage:', error);
      setCreditUsage(prev => ({ ...prev, loading: false }));
    }
  };

  // Effect to update credit balance display when order items change
  useEffect(() => {
    if (selectedCliente && selectedClienteDetails && !creditUsage.loading) {
      setCreditUsage(prev => ({
        ...prev,
        saldoDisponivel: parseFloat(selectedClienteDetails.limiteCredito) - prev.valorUtilizado
      }));
    }
  }, [itensPedido, selectedCliente, selectedClienteDetails, creditUsage.loading, creditUsage.valorUtilizado]);

  // Add product to order items
  const handleAddProduto = async () => {
    if (!selectedProduto || quantidade <= 0) {
      const errorMsg = 'Selecione um produto e quantidade válida!';
      setMessage({ type: 'error', text: errorMsg });
      showSnackbar(errorMsg, 'warning');
      return;
    }

    try {
      setAddingProduct(true);
      
      const produto = produtos.find(p => p.id === selectedProduto);
      const existingItem = itensPedido.find(item => item.produto.id === selectedProduto);

      await new Promise(resolve => setTimeout(resolve, 300));

      if (existingItem) {
        // Update existing item quantity
        setItensPedido(items =>
          items.map(item =>
            item.produto.id === selectedProduto
              ? { ...item, quantidade: item.quantidade + quantidade, subtotal: (item.quantidade + quantidade) * produto.preco }
              : item
          )
        );
        showSnackbar(`Quantidade do produto ${produto.nome} atualizada!`, 'info');
      } else {
        // Add new item
        const newItem = {
          produto: produto,
          quantidade: quantidade,
          subtotal: quantidade * produto.preco
        };
        setItensPedido(items => [...items, newItem]);
        showSnackbar(`Produto ${produto.nome} adicionado ao pedido!`, 'success');
      }

      // Reset form
      setSelectedProduto('');
      setQuantidade(1);
      setMessage({ type: 'success', text: 'Produto adicionado ao pedido!' });
    } catch (error) {
      console.error('Error adding product:', error);
      const errorMsg = 'Erro ao adicionar produto ao pedido.';
      setMessage({ type: 'error', text: errorMsg });
      showSnackbar(errorMsg, 'error');
    } finally {
      setAddingProduct(false);
    }
  };

  // Remove product from order items
  const handleRemoveProduto = (produtoId) => {
    const produto = produtos.find(p => p.id === produtoId);
    setItensPedido(items => items.filter(item => item.produto.id !== produtoId));
    setMessage({ type: 'info', text: 'Produto removido do pedido!' });
    showSnackbar(`Produto ${produto?.nome || 'desconhecido'} removido do pedido!`, 'info');
  };

  // Calculate total value
  const calculateTotal = () => {
    return itensPedido.reduce((total, item) => total + item.subtotal, 0);
  };

  // Submit order
  const handleSubmitPedido = async () => {
    if (!selectedCliente) {
      const errorMsg = 'Selecione um cliente!';
      setMessage({ type: 'error', text: errorMsg });
      showSnackbar(errorMsg, 'warning');
      return;
    }

    if (itensPedido.length === 0) {
      const errorMsg = 'Adicione pelo menos um produto ao pedido!';
      setMessage({ type: 'error', text: errorMsg });
      showSnackbar(errorMsg, 'warning');
      return;
    }

    try {
      setSubmitting(true);
      setMessage({ type: 'info', text: 'Criando pedido...' });
      showSnackbar('Processando pedido... Aguarde!', 'info', 3000);

      const cliente = clientes.find(c => c.id === selectedCliente);
      const totalPedido = calculateTotal();

      const pedidoData = {
        clienteId: selectedCliente,
        itens: itensPedido.map(item => ({
          produtoId: item.produto.id,
          quantidade: item.quantidade
        }))
      };

      const response = await orderService.create(pedidoData);
      
      // Determine response message based on order status
      let successMessage = '';
      let notificationSeverity = 'success';
      
      if (response.data.status === 'APROVADO') {
        const creditInfo = response.data;
        successMessage = `Pedido APROVADO com sucesso! 

  Cliente: ${cliente.nome}
 Limite de crédito: R$ ${parseFloat(creditInfo.limiteCredito || 0).toFixed(2)}
 Valor utilizado: R$ ${parseFloat(creditInfo.valorUtilizado || 0).toFixed(2)}
 Saldo restante: R$ ${parseFloat(creditInfo.saldoDisponivel || 0).toFixed(2)}
 Valor do pedido: R$ ${parseFloat(creditInfo.valorTotal || 0).toFixed(2)}
 ID do Pedido: #${response.data.id || 'N/A'}`;
        notificationSeverity = 'success';
      } else if (response.data.status === 'REJEITADO') {
        // Enhanced credit rejection message with detailed information
        const creditInfo = response.data;
        successMessage = `Pedido REJEITADO por limite de crédito insuficiente!

  Cliente: ${cliente.nome}
 Limite de crédito: R$ ${parseFloat(creditInfo.limiteCredito || 0).toFixed(2)}
 Valor já utilizado: R$ ${parseFloat(creditInfo.valorUtilizado || 0).toFixed(2)}
 Saldo disponível: R$ ${parseFloat(creditInfo.saldoDisponivel || 0).toFixed(2)}
 Valor do pedido: R$ ${parseFloat(creditInfo.valorTotal || 0).toFixed(2)}

  Você precisa de R$ ${(parseFloat(creditInfo.valorTotal || 0) - parseFloat(creditInfo.saldoDisponivel || 0)).toFixed(2)} a mais de crédito disponível.`;
        notificationSeverity = 'error';
      } else {
        successMessage = ` Pedido criado com status: ${response.data.status}
        Cliente: ${cliente.nome}
        Total: R$ ${totalPedido.toFixed(2)}`;
        notificationSeverity = 'info';
      }
      
      // Reset form on success (both approved and rejected orders are "successful" API calls)
      setSelectedCliente('');
      setSelectedProduto('');
      setQuantidade(1);
      setItensPedido([]);
      setSelectedClienteDetails(null);
      setCreditUsage({
        valorUtilizado: 0,
        saldoDisponivel: 0,
        loading: false
      });
      
      setMessage({ 
        type: response.data.status === 'APROVADO' ? 'success' : 'error', 
        text: successMessage 
      });
      
      showSnackbar(successMessage, notificationSeverity, 8000);

    } catch (error) {
      console.error('Error creating order:', error);
      let errorMessage = 'Erro desconhecido ao processar pedido';
      let severity = 'error';
      
      if (!navigator.onLine) {
        errorMessage = 'Sem conexão com a internet. Verifique sua conexão e tente novamente.';
        severity = 'warning';
      } else if (error.code === 'ERR_NETWORK') {
        errorMessage = 'Falha na comunicação com o servidor. O serviço pode estar temporariamente indisponível.';
      } else if (error.response?.status === 408 || error.code === 'ECONNABORTED') {
        errorMessage = 'Timeout na requisição. O servidor demorou muito para responder. Tente novamente.';
      } else if (error.response?.status === 500) {
        errorMessage = 'Erro interno do servidor. Nossa equipe foi notificada. Tente novamente em alguns minutos.';
      } else if (error.response?.status === 503) {
        errorMessage = 'Serviço temporariamente indisponível. Tente novamente em alguns minutos.';
      } else if (error.response?.status === 400) {
        errorMessage = `Dados inválidos: ${error.response?.data?.message || 'Verifique os dados do pedido.'}`;
      } else if (error.response?.status === 422) {
        errorMessage = `Erro de validação: ${error.response?.data?.message || 'Dados do pedido não passaram na validação.'}`;
      } else if (error.response?.data?.message) {
        errorMessage = `Erro do servidor: ${error.response.data.message}`;
      } else if (error.response?.status) {
        errorMessage = `Erro HTTP ${error.response.status}: ${error.response.statusText || 'Erro no servidor'}`;
      } else if (error.message) {
        errorMessage = `Erro de conectividade: ${error.message}`;
      }
      
      const fullErrorMessage = ` Falha ao processar pedido: ${errorMessage}`;
      
      setMessage({ 
        type: 'error', 
        text: fullErrorMessage
      });
      
      showSnackbar(fullErrorMessage, severity, 12000);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Box 
        display="flex" 
        flexDirection="column" 
        justifyContent="center" 
        alignItems="center" 
        minHeight="60vh"
        sx={{ textAlign: 'center' }}
      >
        <CircularProgress size={60} thickness={4} sx={{ mb: 3 }} />
        <Typography variant="h5" gutterBottom>
          Carregando Sistema...
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
          Buscando clientes e produtos...
        </Typography>
        
        {networkError && (
          <Card sx={{ p: 3, maxWidth: 400, backgroundColor: 'error.light', color: 'error.contrastText' }}>
            <Typography variant="h6" gutterBottom>
              Problema de Conexão
            </Typography>
            <Typography variant="body2" sx={{ mb: 2 }}>
              Não foi possível carregar os dados necessários.
            </Typography>
            <Button 
              variant="contained" 
              color="inherit"
              onClick={handleRetryLoading}
              disabled={retryLoading}
              startIcon={retryLoading ? <CircularProgress size={20} /> : null}
              sx={{ 
                backgroundColor: 'error.dark',
                '&:hover': { backgroundColor: 'error.main' }
              }}
            >
              {retryLoading ? 'Tentando...' : 'Tentar Novamente'}
            </Button>
          </Card>
        )}
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 1200, mx: 'auto', p: 3 }}>
      {/* Header with Connection Status */}
      <Box display="flex" justifyContent="space-between" alignItems="center" sx={{ mb: 3 }}>
        <Typography variant="h4" component="h1">
          <CartIcon sx={{ mr: 1, verticalAlign: 'middle' }} />
          Criar Novo Pedido
        </Typography>
        
        <Box display="flex" alignItems="center" gap={1}>
          {/* Refresh Button */}
          <IconButton 
            onClick={handleRetryLoading}
            disabled={retryLoading}
            title="Recarregar dados"
            sx={{ 
              bgcolor: retryLoading ? 'action.disabled' : 'primary.light',
              color: retryLoading ? 'text.disabled' : 'primary.contrastText',
              '&:hover': { 
                bgcolor: retryLoading ? 'action.disabled' : 'primary.main' 
              }
            }}
          >
            {retryLoading ? 
              <CircularProgress size={20} color="inherit" /> : 
              <RefreshIcon />
            }
          </IconButton>
          
          {/* Connection Status Indicator */}
          <Fade in={!isOnline || networkError}>
            <Chip
              icon={isOnline ? <WifiIcon /> : <WifiOffIcon />}
              label={isOnline ? (networkError ? 'Servidor Indisponível' : 'Online') : 'Offline'}
              color={isOnline ? (networkError ? 'warning' : 'success') : 'error'}
              variant="outlined"
              size="small"
            />
          </Fade>
        </Box>
      </Box>

      {/* Messages */}
      {message.text && (
        <Alert severity={message.type} sx={{ mb: 3 }} onClose={() => setMessage({ type: '', text: '' })}>
          {message.text}
        </Alert>
      )}

      <Grid container spacing={3}>
        {/* Customer Selection */}
        <Grid size={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                1. Selecionar Cliente
              </Typography>
              <FormControl fullWidth disabled={retryLoading || (!isOnline && networkError)}>
                <InputLabel>Cliente</InputLabel>
                <Select
                  value={selectedCliente}
                  onChange={async (e) => {
                    const clienteId = e.target.value;
                    setSelectedCliente(clienteId);
                    // Find and set the selected client details for credit display
                    const cliente = clientes.find(c => c.id === clienteId);
                    setSelectedClienteDetails(cliente || null);
                    
                    // Clear any existing items when changing client to avoid confusion
                    if (clienteId !== selectedCliente) {
                      setItensPedido([]);
                    }
                    
                    // Fetch real-time credit usage for the selected customer
                    if (clienteId && isOnline && !networkError) {
                      await fetchCustomerCreditUsage(clienteId);
                    } else if (clienteId) {
                      // Reset credit usage state if offline
                      setCreditUsage({
                        valorUtilizado: 0,
                        saldoDisponivel: cliente ? parseFloat(cliente.limiteCredito) : 0,
                        loading: false
                      });
                    }
                  }}
                  label="Cliente"
                >
                  {clientes.map((cliente) => (
                    <MenuItem key={cliente.id} value={cliente.id}>
                      <Box>
                        <Typography variant="body1">{cliente.nome}</Typography>
                        <Typography variant="caption" color="text.secondary">
                          Limite: R$ {parseFloat(cliente.limiteCredito).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                        </Typography>
                      </Box>
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </CardContent>
          </Card>
        </Grid>

        {/* Credit Information Display */}
        <Grid size={12}>
          <Fade in={true}>
            <Card sx={{ bgcolor: 'primary.light', color: 'primary.contrastText', mb: 1 }}>
              <CardContent>
                <Typography variant="h6" gutterBottom sx={{ display: 'flex', alignItems: 'center' }}>
                  Informações de Crédito {selectedClienteDetails && `- ${selectedClienteDetails.nome}`}
                </Typography>
                {selectedClienteDetails ? (
                  <>
                    <Grid container spacing={2}>
                      <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <Box sx={{ textAlign: 'center', p: 1 }}>
                          <Typography variant="caption" display="block">
                            Limite de Crédito
                          </Typography>
                          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                            R$ {parseFloat(selectedClienteDetails.limiteCredito || 0).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                          </Typography>
                        </Box>
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <Box sx={{ textAlign: 'center', p: 1 }}>
                          <Typography variant="caption" display="block">
                            Valor Utilizado
                          </Typography>
                          {creditUsage.loading ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '32px' }}>
                              <CircularProgress size={20} color="inherit" />
                              <Typography variant="body2" sx={{ ml: 1 }}>
                                Carregando...
                              </Typography>
                            </Box>
                          ) : (
                            <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                              R$ {creditUsage.valorUtilizado.toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                            </Typography>
                          )}
                        </Box>
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <Box sx={{ textAlign: 'center', p: 1 }}>
                          <Typography variant="caption" display="block">
                            Saldo Disponível
                          </Typography>
                          {creditUsage.loading ? (
                            <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '32px' }}>
                              <CircularProgress size={20} color="inherit" />
                              <Typography variant="body2" sx={{ ml: 1 }}>
                                Carregando...
                              </Typography>
                            </Box>
                          ) : (
                            <Typography 
                              variant="h6" 
                              sx={{ 
                                fontWeight: 'bold',
                                color: creditUsage.saldoDisponivel >= calculateTotal() ? 'success.main' : 'error.main'
                              }}
                            >
                              R$ {creditUsage.saldoDisponivel.toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                            </Typography>
                          )}
                        </Box>
                      </Grid>
                      <Grid size={{ xs: 12, sm: 6, md: 3 }}>
                        <Box sx={{ textAlign: 'center', p: 1 }}>
                          <Typography variant="caption" display="block">
                            Pedido Atual
                          </Typography>
                          <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                            R$ {calculateTotal().toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                          </Typography>
                        </Box>
                      </Grid>
                    </Grid>
                    
                    {/* Credit utilization progress bar */}
                    <Box sx={{ mt: 2 }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                        <Typography variant="caption">Utilização do Crédito</Typography>
                        <Typography variant="caption">
                          {creditUsage.loading ? 
                            'Calculando...' :
                            `${(((creditUsage.valorUtilizado + calculateTotal()) / parseFloat(selectedClienteDetails.limiteCredito || 1)) * 100).toFixed(1)}%`
                          }
                        </Typography>
                      </Box>
                      <Box
                        sx={{
                          width: '100%',
                          height: 8,
                          bgcolor: 'rgba(255,255,255,0.3)',
                          borderRadius: 4,
                          overflow: 'hidden'
                        }}
                      >
                        <Box
                          sx={{
                            height: '100%',
                            bgcolor: (() => {
                              if (creditUsage.loading) return 'info.main';
                              const utilizacao = (creditUsage.valorUtilizado + calculateTotal()) / parseFloat(selectedClienteDetails.limiteCredito || 1);
                              return utilizacao <= 0.8 ? 'success.main' : utilizacao <= 0.95 ? 'warning.main' : 'error.main';
                            })(),
                            width: `${Math.min(100, creditUsage.loading ? 0 : ((creditUsage.valorUtilizado + calculateTotal()) / parseFloat(selectedClienteDetails.limiteCredito || 1) * 100))}%`,
                            transition: 'all 0.3s ease-in-out'
                          }}
                        />
                      </Box>
                    </Box>
                  </>
                ) : (
                  <Box sx={{ textAlign: 'center', p: 3 }}>
                    <Typography variant="body1" sx={{ opacity: 0.7 }}>
                      Selecione um cliente acima para visualizar as informações de crédito
                    </Typography>
                    {/* <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 2, opacity: 0.5 }}>
                      <Chip label=" Limite" variant="outlined" size="small" />
                      <Chip label="Utilizado" variant="outlined" size="small" />
                      <Chip label="Disponível" variant="outlined" size="small" />
                      <Chip label="Pedido" variant="outlined" size="small" />
                    </Box> */}
                  </Box>
                )}
              </CardContent>
            </Card>
          </Fade>
        </Grid>

        {/* Product Selection */}
        <Grid size={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                2. Adicionar Produtos
              </Typography>
              <Grid container spacing={2} alignItems="center">
                <Grid size={{ xs: 12, md: 5 }}>
                  <FormControl fullWidth disabled={retryLoading || (!isOnline && networkError)}>
                    <InputLabel>Produto</InputLabel>
                    <Select
                      value={selectedProduto}
                      onChange={(e) => setSelectedProduto(e.target.value)}
                      label="Produto"
                      MenuProps={{
                        PaperProps: {
                          style: {
                            maxHeight: 300,
                          },
                        },
                      }}
                    >
                      {produtos.map((produto) => (
                        <MenuItem key={produto.id} value={produto.id}>
                          <Box>
                            <Typography variant="body1">{produto.nome}</Typography>
                            <Typography variant="caption" color="text.secondary">
                              R$ {parseFloat(produto.preco).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                            </Typography>
                          </Box>
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                </Grid>
                <Grid size={{ xs: 12, md: 3 }}>
                  <TextField
                    fullWidth
                    label="Quantidade"
                    type="number"
                    value={quantidade}
                    onChange={(e) => setQuantidade(parseInt(e.target.value) || 1)}
                    inputProps={{ min: 1, max: 999 }}
                    disabled={retryLoading || (!isOnline && networkError)}
                  />
                </Grid>
                <Grid size={{ xs: 12, md: 4 }}>
                  <Button
                    fullWidth
                    variant="contained"
                    startIcon={addingProduct ? <CircularProgress size={20} color="inherit" /> : <AddIcon />}
                    onClick={handleAddProduto}
                    disabled={!selectedProduto || quantidade <= 0 || addingProduct || retryLoading || (!isOnline && networkError)}
                    sx={{ py: 1.5 }}
                  >
                    {addingProduct ? 'Adicionando...' : 'Adicionar Produto'}
                  </Button>
                </Grid>
              </Grid>
            </CardContent>
          </Card>
        </Grid>

        {/* Order Items Table */}
        <Grid size={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                3. Itens do Pedido
              </Typography>
              {itensPedido.length > 0 ? (
                <>
                  <TableContainer component={Paper} variant="outlined">
                    <Table>
                      <TableHead>
                        <TableRow>
                          <TableCell><strong>Produto</strong></TableCell>
                          <TableCell align="center"><strong>Quantidade</strong></TableCell>
                          <TableCell align="right"><strong>Preço Unitário</strong></TableCell>
                          <TableCell align="right"><strong>Subtotal</strong></TableCell>
                          <TableCell align="center"><strong>Ações</strong></TableCell>
                        </TableRow>
                      </TableHead>
                      <TableBody>
                        {itensPedido.map((item, index) => (
                          <TableRow key={index}>
                            <TableCell>{item.produto.nome}</TableCell>
                            <TableCell align="center">{item.quantidade}</TableCell>
                            <TableCell align="right">
                              R$ {parseFloat(item.produto.preco).toFixed(2)}
                            </TableCell>
                            <TableCell align="right">
                              R$ {item.subtotal.toFixed(2)}
                            </TableCell>
                            <TableCell align="center">
                              <IconButton
                                color="error"
                                onClick={() => handleRemoveProduto(item.produto.id)}
                              >
                                <DeleteIcon />
                              </IconButton>
                            </TableCell>
                          </TableRow>
                        ))}
                        <TableRow>
                          <TableCell colSpan={3}>
                            <Typography variant="h6"><strong>Total do Pedido:</strong></Typography>
                          </TableCell>
                          <TableCell align="right">
                            <Typography variant="h6" color="primary">
                              <strong>R$ {calculateTotal().toFixed(2)}</strong>
                            </Typography>
                          </TableCell>
                          <TableCell />
                        </TableRow>
                      </TableBody>
                    </Table>
                  </TableContainer>
                </>
              ) : (
                <Alert severity="info">
                  Nenhum produto adicionado ao pedido ainda.
                </Alert>
              )}
            </CardContent>
          </Card>
        </Grid>

        {/* Submit Button */}
        <Grid size={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                4. Finalizar Pedido
              </Typography>
              <Divider sx={{ mb: 2 }} />
              
              {/* Credit balance warning */}
              {selectedCliente && itensPedido.length > 0 && !creditUsage.loading && (
                <Box sx={{ mb: 2 }}>
                  {creditUsage.saldoDisponivel < calculateTotal() ? (
                    <Alert severity="error" sx={{ mb: 2 }}>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                        Crédito Insuficiente!
                      </Typography>
                      <Typography variant="body2">
                        Valor do pedido: R$ {calculateTotal().toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                        <br />
                        Saldo disponível: R$ {creditUsage.saldoDisponivel.toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                        <br />
                        Déficit: R$ {(calculateTotal() - creditUsage.saldoDisponivel).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                      </Typography>
                      <Typography variant="body2" sx={{ mt: 1, fontStyle: 'italic' }}>
                        Este pedido será rejeitado por exceder o limite de crédito disponível.
                      </Typography>
                    </Alert>
                  ) : creditUsage.saldoDisponivel < calculateTotal() * 1.1 ? (
                    <Alert severity="warning" sx={{ mb: 2 }}>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                        Saldo Baixo
                      </Typography>
                      <Typography variant="body2">
                        Você está próximo do seu limite de crédito. Saldo disponível após este pedido: R$ {(creditUsage.saldoDisponivel - calculateTotal()).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                      </Typography>
                    </Alert>
                  ) : (
                    <Alert severity="success" sx={{ mb: 2 }}>
                      <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                        Crédito OK
                      </Typography>
                      <Typography variant="body2">
                        Pedido aprovado! Saldo disponível após este pedido: R$ {(creditUsage.saldoDisponivel - calculateTotal()).toLocaleString('pt-BR', {minimumFractionDigits: 2})}
                      </Typography>
                    </Alert>
                  )}
                </Box>
              )}
              
              <Box display="flex" justifyContent="center">
                <Button
                  variant="contained"
                  color="primary"
                  size="large"
                  startIcon={submitting ? <CircularProgress size={20} color="inherit" /> : <CartIcon />}
                  onClick={handleSubmitPedido}
                  disabled={!selectedCliente || itensPedido.length === 0 || submitting || (!isOnline && networkError)}
                  sx={{ 
                    minWidth: 220, 
                    py: 1.5,
                    background: submitting ? 'linear-gradient(45deg, #FE6B8B 30%, #FF8E53 90%)' : undefined,
                    '&:disabled': {
                      background: (!isOnline && networkError) ? '#ffcdd2' : undefined
                    }
                  }}
                >
                  {submitting ? 'Processando Pedido...' : 'Cadastrar Pedido'}
                </Button>
              </Box>
              {selectedCliente && itensPedido.length > 0 && isOnline && !networkError && (
                <Typography variant="body2" color="text.secondary" align="center" sx={{ mt: 2 }}>
                  O pedido será enviado para validação de limite de crédito
                </Typography>
              )}
              {(!isOnline || networkError) && (
                <Alert severity="warning" sx={{ mt: 2 }}>
                  <Typography variant="body2">
                    {!isOnline ? 
                      'Sem conexão com a internet. Conecte-se para enviar o pedido.' :
                      'Servidor temporariamente indisponível. Tente novamente em alguns momentos.'
                    }
                  </Typography>
                </Alert>
              )}
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Enhanced Snackbar for API Response Feedback */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={snackbar.duration}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbar.severity}
          variant="filled"
          sx={{ 
            width: '100%',
            maxWidth: '500px',
            '& .MuiAlert-icon': {
              fontSize: '1.5rem'
            },
            '& .MuiAlert-message': {
              fontSize: '1rem',
              fontWeight: 'medium',
              whiteSpace: 'pre-line'
            }
          }}
          icon={
            snackbar.severity === 'success' ? <CheckCircleIcon fontSize="inherit" /> :
            snackbar.severity === 'error' ? <ErrorIcon fontSize="inherit" /> :
            snackbar.severity === 'warning' ? <WarningIcon fontSize="inherit" /> :
            <InfoIcon fontSize="inherit" />
          }
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
  );
};

export default PedidoForm;
