import React, { useState, useEffect } from 'react';
import api from '../api/api';
import { customerService, productService } from '../api/services';
import { 
  Box, 
  Typography, 
  CircularProgress, 
  Alert, 
  Card, 
  CardContent, 
  Grid 
} from '@mui/material';

const ApiTest = () => {
  const [healthStatus, setHealthStatus] = useState('');
  const [customers, setCustomers] = useState([]);
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const testApiConnections = async () => {
      try {
        setLoading(true);
        
        // Test basic API connection
        const healthResponse = await api.get('/api/health');
        setHealthStatus(healthResponse.data);
        
        // Test customer service
        const customersResponse = await customerService.getAll();
        setCustomers(customersResponse.data);
        
        // Test product service
        const productsResponse = await productService.getAll();
        setProducts(productsResponse.data);
        
        setError(null);
      } catch (err) {
        setError(`API Connection Failed: ${err.message}`);
        console.error('API test failed:', err);
      } finally {
        setLoading(false);
      }
    };

    testApiConnections();
  }, []);

  return (
    <Box sx={{ mt: 4, p: 3 }}>
      <Typography variant="h6" gutterBottom>
        API Connection & Services Test
      </Typography>
      
      {loading ? (
        <Box display="flex" justifyContent="center" mt={2}>
          <CircularProgress />
        </Box>
      ) : error ? (
        <Alert severity="error">{error}</Alert>
      ) : (
        <Grid container spacing={3}>
          {/* Health Check */}
          <Grid item xs={12}>
            <Alert severity="success">
              ✅ API Connected Successfully: {healthStatus}
            </Alert>
          </Grid>
          
          {/* Customers Test */}
          <Grid item xs={12} md={6}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Customers ({customers.length})
                </Typography>
                {customers.length > 0 ? (
                  customers.slice(0, 3).map((customer) => (
                    <Typography key={customer.id} variant="body2">
                      • {customer.nome} (Limite: R$ {customer.limiteCredito})
                    </Typography>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    No customers found
                  </Typography>
                )}
              </CardContent>
            </Card>
          </Grid>
          
          {/* Products Test */}
          <Grid item xs={12} md={6}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  Products ({products.length})
                </Typography>
                {products.length > 0 ? (
                  products.slice(0, 3).map((product) => (
                    <Typography key={product.id} variant="body2">
                      • {product.nome} (R$ {product.preco})
                    </Typography>
                  ))
                ) : (
                  <Typography variant="body2" color="text.secondary">
                    No products found
                  </Typography>
                )}
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      )}
    </Box>
  );
};

export default ApiTest;
