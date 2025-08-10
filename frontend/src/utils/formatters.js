// Utility functions for formatting data

// Format currency values (Brazilian Real)
export const formatCurrency = (value) => {
  if (!value && value !== 0) return 'R$ 0,00';
  
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(parseFloat(value));
};

// Format date to Brazilian format
export const formatDate = (date) => {
  if (!date) return '';
  
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  }).format(new Date(date));
};

// Format date to Brazilian format (date only)
export const formatDateOnly = (date) => {
  if (!date) return '';
  
  return new Intl.DateTimeFormat('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  }).format(new Date(date));
};

// Format order status to Portuguese
export const formatOrderStatus = (status) => {
  const statusMap = {
    'APROVADO': 'Aprovado',
    'REJEITADO': 'Rejeitado',
    'PENDENTE': 'Pendente'
  };
  
  return statusMap[status] || status;
};

// Get status color for UI components
export const getStatusColor = (status) => {
  const colorMap = {
    'APROVADO': 'success',
    'REJEITADO': 'error',
    'PENDENTE': 'warning'
  };
  
  return colorMap[status] || 'default';
};

// Parse currency string to number
export const parseCurrency = (currencyString) => {
  if (!currencyString) return 0;
  
  return parseFloat(
    currencyString
      .replace('R$', '')
      .replace(/\./g, '')
      .replace(',', '.')
      .trim()
  );
};

// Validate email format
export const isValidEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

// Validate Brazilian CPF format (for future use)
export const isValidCPF = (cpf) => {
  cpf = cpf.replace(/[^\d]/g, '');
  return cpf.length === 11;
};
