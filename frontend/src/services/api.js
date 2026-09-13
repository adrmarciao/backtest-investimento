import axios from 'axios';

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getAssets = () => api.get('/assets');
export const saveAsset = (asset) => api.post('/assets', asset);
export const deleteAsset = (ticker) => api.delete(`/assets/${ticker}`);

export const getCriteria = () => api.get('/criteria');
export const saveCriteria = (criteria) => api.put('/criteria', criteria);

export const getIndicators = (ticker) => api.get(`/assets/${ticker}/indicators`);
export const saveIndicators = (ticker, indicators) => api.post(`/assets/${ticker}/indicators`, indicators);
export const updateIndicators = (ticker, year, indicators) => api.put(`/assets/${ticker}/indicators/${year}`, indicators);
export const deleteIndicators = (ticker, year) => api.delete(`/assets/${ticker}/indicators/${year}`);

export const executeBacktest = (inicio, fim) => api.post('/backtest', { inicio, fim });
export const getBacktestResults = () => api.get('/backtest');
export const getBacktestResultById = (id) => api.get(`/backtest/${id}`);

export default api;
