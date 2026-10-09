import React, { useState, useEffect, useRef } from 'react';
import { Box, Typography, Button, CircularProgress, Alert } from '@mui/material';
import CloudUploadIcon from '@mui/icons-material/CloudUpload';
import CropIcon from '@mui/icons-material/Crop';
import ReactCrop from 'react-image-crop';
import 'react-image-crop/dist/ReactCrop.css';

export default function UploadPage() {
  const [file, setFile] = useState(null);
  const [imgSrc, setImgSrc] = useState('');
  const imgRef = useRef(null);
  const [crop, setCrop] = useState();
  const [completedCrop, setCompletedCrop] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState('');
  const [token, setToken] = useState('');

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    setToken(params.get('token') || '');
  }, []);

  const handleFileChange = (e) => {
    if (e.target.files && e.target.files.length > 0) {
      const selectedFile = e.target.files[0];
      setFile(selectedFile);
      setCrop(undefined);
      setCompletedCrop(null);
      setSuccess(false);
      
      const reader = new FileReader();
      reader.addEventListener('load', () => {
        setImgSrc(reader.result?.toString() || '');
      });
      reader.readAsDataURL(selectedFile);
    }
  };

  // Recorta (se houver seleção) na resolução original, depois reduz o maior lado
  // para MAX_DIMENSION e comprime em JPEG — reduz drasticamente o payload enviado à IA.
  const MAX_DIMENSION = 1600;
  const JPEG_QUALITY = 0.8;

  const prepareImage = (image, cropObj) => {
    const scaleX = image.naturalWidth / image.width;
    const scaleY = image.naturalHeight / image.height;

    const hasCrop = cropObj && cropObj.width > 0 && cropObj.height > 0;
    const sx = hasCrop ? cropObj.x * scaleX : 0;
    const sy = hasCrop ? cropObj.y * scaleY : 0;
    const sw = hasCrop ? cropObj.width * scaleX : image.naturalWidth;
    const sh = hasCrop ? cropObj.height * scaleY : image.naturalHeight;

    const ratio = Math.min(1, MAX_DIMENSION / Math.max(sw, sh));
    const canvas = document.createElement('canvas');
    canvas.width = Math.round(sw * ratio);
    canvas.height = Math.round(sh * ratio);

    const ctx = canvas.getContext('2d');
    ctx.imageSmoothingQuality = 'high';
    ctx.drawImage(image, sx, sy, sw, sh, 0, 0, canvas.width, canvas.height);

    return new Promise((resolve, reject) => {
      canvas.toBlob((blob) => {
        if (!blob) {
          reject(new Error('Falha ao processar a imagem'));
          return;
        }
        resolve(blob);
      }, 'image/jpeg', JPEG_QUALITY);
    });
  };

  const handleUpload = async () => {
    if (!file && !imgSrc) return;
    setUploading(true);
    setError('');
    
    try {
      let finalFile = file;
      
      if (imgRef.current) {
        finalFile = await prepareImage(imgRef.current, completedCrop);
      }

      const formData = new FormData();
      formData.append('file', finalFile, 'receipt.jpg');
      formData.append('token', token);

      const res = await fetch('/api/expenses/upload', {
        method: 'POST',
        body: formData,
      });

      if (!res.ok) {
        throw new Error('Falha no upload');
      }

      setSuccess(true);
      setFile(null);
      setImgSrc('');
    } catch (err) {
      setError(err.message || 'Ocorreu um erro');
    } finally {
      setUploading(false);
    }
  };

  return (
    <Box sx={{ p: 3, display: 'flex', flexDirection: 'column', alignItems: 'center', minHeight: '100vh', bgcolor: '#0f141d', color: '#fff' }}>
      <Typography variant="h5" sx={{ mb: 4, fontWeight: 'bold' }}>
        Scanner de Recibo
      </Typography>

      {success ? (
        <Alert severity="success" sx={{ mb: 3 }}>Recibo enviado com sucesso!</Alert>
      ) : null}

      {error ? (
        <Alert severity="error" sx={{ mb: 3 }}>{error}</Alert>
      ) : null}

      <Button
        variant="contained"
        component="label"
        startIcon={<CloudUploadIcon />}
        sx={{ mb: 3, py: 2, px: 4, borderRadius: '16px' }}
      >
        Tirar Foto / Escolher
        <input
          type="file"
          accept="image/*"
          capture="environment"
          hidden
          onChange={handleFileChange}
        />
      </Button>

      {imgSrc && (
        <Box sx={{ mb: 3, maxWidth: '100%', overflow: 'hidden', textAlign: 'center' }}>
          <Typography variant="body2" sx={{ mb: 1, color: '#a0a3ab' }}>
            Arraste para recortar e enviar apenas o essencial (opcional)
          </Typography>
          <ReactCrop 
            crop={crop} 
            onChange={c => setCrop(c)} 
            onComplete={c => setCompletedCrop(c)}
          >
            <img 
              ref={imgRef}
              src={imgSrc} 
              alt="Crop preview" 
              style={{ maxHeight: '60vh', maxWidth: '100%', objectFit: 'contain' }} 
            />
          </ReactCrop>
        </Box>
      )}

      {(file || imgSrc) && (
        <Button
          variant="contained"
          color="secondary"
          disabled={uploading}
          onClick={handleUpload}
          startIcon={<CropIcon />}
          sx={{ py: 1.5, px: 6, borderRadius: '16px' }}
        >
          {uploading ? <CircularProgress size={24} color="inherit" /> : 'Enviar'}
        </Button>
      )}
    </Box>
  );
}
