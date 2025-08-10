import React from 'react';
import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { Box, Typography, Container } from '@mui/material';
import theme from './theme';
import PedidoForm from './pages/PedidoForm';
import './App.css';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ minHeight: '100vh', backgroundColor: 'background.default' }}>
        <Container maxWidth="lg">
          <Box sx={{ py: 4 }}>
            <Typography variant="h1" component="h1" gutterBottom align="center">
              Sistema de Gerenciamento de Pedidos
            </Typography>
            <Typography variant="h5" component="p" align="center" color="text.secondary" sx={{ mb: 4 }}>
              TechnoSoftware Assetment
            </Typography>
            
            {/* PedidoForm Component */}
            <PedidoForm />
          </Box>
        </Container>
      </Box>
    </ThemeProvider>
  );
}

export default App;
