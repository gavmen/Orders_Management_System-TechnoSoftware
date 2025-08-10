# Orders Management System - Frontend

React-based frontend for the Customer Orders Management System. This application provides an intuitive interface for managing customer orders with real-time credit limit validation.

## Technology Stack

- **React 19.1.1** - Frontend framework
- **Material-UI 7.3.1** - Component library and design system
- **Axios 1.11.0** - HTTP client for API communication
- **JavaScript ES6+** - Programming language

## Features

- **Order Creation**: Interactive form for creating customer orders
- **Credit Validation**: Real-time credit limit checking and balance display
- **Product Selection**: Dynamic product catalog with pricing
- **Customer Management**: Customer selection with credit information
- **Responsive Design**: Works on desktop and mobile devices
- **Error Handling**: Comprehensive error messages and network status monitoring

## Prerequisites

- Node.js 18 or higher
- NPM 9 or higher
- Backend API running on port 8080

## Installation

```bash
cd frontend
npm install
```

## Development

```bash
npm start
```

Runs the development server on http://localhost:3000

## Build for Production

```bash
npm run build
```

Creates optimized production build in the `build` folder.

## Project Structure

```
src/
├── api/                 # API configuration and services
│   ├── api.js          # Axios configuration
│   └── services.js     # Service layer for API calls
├── components/         # Reusable UI components
├── pages/             # Page components
│   └── PedidoForm.js  # Main order form component
├── services/          # Business logic services
├── utils/             # Utility functions
├── theme.js           # Material-UI theme configuration
└── App.js             # Root application component
```

## API Integration

The frontend communicates with the backend API through service layers:

- **customerService**: Customer operations and credit balance
- **productService**: Product catalog management
- **orderService**: Order creation and management

## Configuration

API base URL is configured in `src/api/api.js` and defaults to `http://localhost:8080/api`.

## Testing

```bash
npm test
```

Runs the test runner in interactive watch mode.

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)
