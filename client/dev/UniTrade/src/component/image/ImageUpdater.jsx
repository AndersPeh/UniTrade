import React, { useState, useRef, useEffect } from "react";
import { useDispatch } from "react-redux";
import { Button, Modal, Spinner } from "react-bootstrap";
import { toast, ToastContainer } from "react-toastify";
import {
  uploadImages,
  updateProductImage,
} from "../../store/features/imageSlice";

// ImageUpdater Component
// This component allows users to upload a new image or update an existing image for a product
// It displays a modal with a file input and image preview
// On form submission, the selected image is uploaded or updated via a Redux action
const ImageUpdater = ({
  show,
  handleClose,
  selectedImageId,
  productId,
  selectedImage,
}) => {
  const fileInputRef = useRef(null);
  const dispatch = useDispatch();
  const [selectedFile, setSelectedFile] = useState(null);
  const [imagePreview, setImagePreview] = useState(null);
  const [isUploading, setIsUploading] = useState(false);

  useEffect(() => {
    if (selectedImage) {
      setImagePreview(selectedImage.imageUrl);
    } else {
      setImagePreview(null);
    }
  }, [selectedImage]);

  // Reset states when modal closes
  useEffect(() => {
    if (!show) {
      setSelectedFile(null);
      setImagePreview(selectedImage?.imageUrl || null);
      setIsUploading(false);
    }
  }, [show, selectedImage]);

  const handleFileChange = (event) => {
    const file = event.target.files[0];
    setSelectedFile(file);
    if (file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target.result);
      };
      reader.readAsDataURL(file);
    } else {
      setImagePreview(null);
    }
  };

  const handleImageAction = async () => {
    if (!selectedFile) {
      toast.warn("Please select an image.");
      return;
    }

    setIsUploading(true);

    try {
      let result;
      if (selectedImageId) {
        // Updating existing image
        result = await dispatch(
          updateProductImage({ 
            imageId: selectedImageId, 
            file: selectedFile, 
            productId 
          })
        ).unwrap();
        toast.success(result.message || "Image updated successfully!");
      } else {
        // Uploading new image
        result = await dispatch(
          uploadImages({ productId, files: selectedFile })
        ).unwrap();
        toast.success(result.message || "Image uploaded successfully!");
      }
      
      // Reset form and close modal after successful upload
      setTimeout(() => {
        setSelectedFile(null);
        setImagePreview(null);
        handleClose();
      }, 1500);

    } catch (error) {
      console.error("Image upload error:", error);
      toast.error(error?.message || "Failed to upload image. Please try again.");
    } finally {
      setIsUploading(false);
    }
  };

  const handleModalClose = () => {
    if (!isUploading) {
      handleClose();
    }
  };

  return (
    <>
      <ToastContainer />
      <Modal 
        show={show} 
        onHide={handleModalClose}
        backdrop={isUploading ? "static" : true}
        keyboard={!isUploading}
      >
        <Modal.Header 
          closeButton={!isUploading} 
          style={{ backgroundColor: "whitesmoke" }}
        >
          <Modal.Title>
            {selectedImageId ? "Update Product Image" : "Add Product Image"}
          </Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <div>
            <p>
              {selectedImageId
                ? "Select a new image to replace the current one:"
                : "Select the image to be added:"}
            </p>
            <input
              type='file'
              accept='image/*'
              className='form-control'
              ref={fileInputRef}
              onChange={handleFileChange}
              disabled={isUploading}
            />
            <div className='image-preview-container mt-3'>
              {imagePreview && (
                <img
                  src={imagePreview}
                  alt='Image Preview'
                  className='img-fluid image-preview'
                  style={{ 
                    maxHeight: '300px', 
                    objectFit: 'contain',
                    opacity: isUploading ? 0.5 : 1
                  }}
                />
              )}
            </div>
            
            {isUploading && (
              <div className='text-center mt-3'>
                <Spinner animation="border" variant="primary" />
                <p className='text-muted mt-2'>
                  {selectedImageId ? "Updating image..." : "Uploading image..."}
                </p>
              </div>
            )}
          </div>
        </Modal.Body>

        <Modal.Footer style={{ backgroundColor: "whitesmoke" }}>
          <Button 
            variant='secondary' 
            onClick={handleModalClose}
            disabled={isUploading}
          >
            Close
          </Button>
          <Button 
            variant='secondary' 
            onClick={handleImageAction}
            disabled={isUploading || !selectedFile}
            style={{
              minWidth: '120px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              gap: '8px'
            }}
          >
            {isUploading ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                />
                <span>
                  {selectedImageId ? "Updating..." : "Uploading..."}
                </span>
              </>
            ) : (
              <span>
                {selectedImageId ? "Save Changes" : "Upload Image"}
              </span>
            )}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default ImageUpdater;