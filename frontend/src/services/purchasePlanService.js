import api from './api';

/**
 * Serviço REST para o módulo Plano de Compras (Purchase Plan Allocator)
 */
export const getConfig = () => api.get('/purchase-plan/config');

export const saveConfig = (config) => api.put('/purchase-plan/config', config);

export const getAssets = () => api.get('/purchase-plan/assets');

export const saveAllAssets = (assets) => api.put('/purchase-plan/assets', assets);

export const saveAsset = (asset) => api.post('/purchase-plan/assets', asset);

export const deleteAsset = (ticker) => api.delete(`/purchase-plan/assets/${ticker}`);

export const syncQuotes = () => api.post('/purchase-plan/sync-quotes');

export const syncFundamentals = (token) => api.post('/purchase-plan/sync-fundamentals', { token });

export const getBenchmarkStatus = (benchmark = 'IBOV') =>
  api.get(`/purchase-plan/benchmark-status?benchmark=${encodeURIComponent(benchmark)}`);

export const getAllocation = () => api.get('/purchase-plan/allocation');

export default {
  getConfig,
  saveConfig,
  getAssets,
  saveAllAssets,
  saveAsset,
  deleteAsset,
  syncQuotes,
  syncFundamentals,
  getBenchmarkStatus,
  getAllocation,
};
