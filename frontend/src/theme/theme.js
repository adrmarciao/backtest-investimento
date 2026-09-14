import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#a5c8ff',
      light: '#d4e3ff',
      dark: '#004785',
      contrastText: '#00315e',
    },
    secondary: {
      main: '#bec6dc',
      light: '#dae2f9',
      dark: '#3e4759',
      contrastText: '#283041',
    },
    tertiary: {
      main: '#ddbce0',
      light: '#fad8fd',
      dark: '#573e5c',
      contrastText: '#3f2844',
    },
    error: {
      main: '#ffb4ab',
      light: '#ffdad6',
      dark: '#93000a',
      contrastText: '#690005',
    },
    warning: {
      main: '#f59e0b',
      light: '#fbbf24',
      dark: '#b45309',
      contrastText: '#1f1300',
    },
    success: {
      main: '#10b981',
      light: '#34d399',
      dark: '#047857',
      contrastText: '#002111',
    },
    background: {
      default: '#0f141d',
      paper: '#1b202a',
    },
    text: {
      primary: '#e0e2ed',
      secondary: '#c3c6cf',
      disabled: '#8d9199',
    },
    divider: '#43474e',
  },
  typography: {
    fontFamily: "'Plus Jakarta Sans', Roboto, -apple-system, BlinkMacSystemFont, sans-serif",
    h1: {
      fontWeight: 800,
      letterSpacing: '-0.02em',
    },
    h2: {
      fontWeight: 700,
      letterSpacing: '-0.02em',
    },
    h3: {
      fontWeight: 700,
      letterSpacing: '-0.01em',
    },
    h4: {
      fontWeight: 600,
    },
    h5: {
      fontWeight: 600,
    },
    h6: {
      fontWeight: 600,
    },
    button: {
      textTransform: 'none',
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundColor: '#0f141d',
          color: '#e0e2ed',
          backgroundImage:
            'radial-gradient(at 0% 0%, rgba(165, 200, 255, 0.06) 0px, transparent 50%), radial-gradient(at 100% 100%, rgba(221, 188, 224, 0.06) 0px, transparent 50%)',
          backgroundAttachment: 'fixed',
          minHeight: '100vh',
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        root: {
          borderRadius: 9999,
          padding: '8px 20px',
        },
        containedPrimary: {
          background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 50%, #ec4899 100%)',
          color: '#ffffff',
          boxShadow: '0 4px 15px rgba(99, 102, 241, 0.35)',
          '&:hover': {
            boxShadow: '0 6px 20px rgba(99, 102, 241, 0.5)',
          },
        },
        outlined: {
          borderColor: '#43474e',
          color: '#e0e2ed',
          '&:hover': {
            borderColor: '#8d9199',
            backgroundColor: '#262a35',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          backgroundColor: '#1b202a',
          backgroundImage: 'none',
          borderRadius: 16,
          border: '1px solid #43474e',
          boxShadow: '0 2px 12px rgba(0, 0, 0, 0.3)',
        },
      },
    },
    MuiPaper: {
      styleOverrides: {
        root: {
          backgroundColor: '#1b202a',
          backgroundImage: 'none',
        },
      },
    },
    MuiOutlinedInput: {
      styleOverrides: {
        root: {
          backgroundColor: '#171c26',
          borderRadius: 8,
          '& .MuiOutlinedInput-notchedOutline': {
            borderColor: '#43474e',
          },
          '&:hover .MuiOutlinedInput-notchedOutline': {
            borderColor: '#8d9199',
          },
          '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
            borderColor: '#a5c8ff',
          },
        },
      },
    },
    MuiTableHead: {
      styleOverrides: {
        root: {
          backgroundColor: '#171c26',
        },
      },
    },
    MuiTableCell: {
      styleOverrides: {
        root: {
          borderColor: '#43474e',
          padding: '12px 16px',
        },
        head: {
          fontWeight: 600,
          color: '#bec6dc',
        },
      },
    },
  },
});

export default theme;
