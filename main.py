from fastapi import FastAPI, File, UploadFile, Form
import torch
import numpy as np
from PIL import Image
from torchvision import transforms
from io import BytesIO
import os
import pydicom
from PIL import ImageOps

app = FastAPI()

@app.post("/predict")
async def predict(file: UploadFile = File(...)):

    # Load model
    model = torch.hub.load('mateuszbuda/brain-segmentation-pytorch', 'unet',
    in_channels=3, out_channels=1, init_features=32, pretrained=True)

    # Read file
    contents = await file.read()
    
    # Check if the file is a DICOM file
    if file.filename.endswith('.dcm'):
        # Process as a DICOM file
        image = convert_dcm_to_png(contents)
    else:
        # Process as a regular image file
        # image = Image.open(contents)
        image = Image.open(BytesIO(contents))
    
    # Use this function before preprocessing
    image = prepare_image_for_model(image)

    m, s = np.mean(image, axis=(0, 1)), np.std(image, axis=(0, 1))
    preprocess = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize(mean=m, std=s),
    ])
    input_tensor = preprocess(image)
    input_batch = input_tensor.unsqueeze(0)

    if torch.cuda.is_available():
        input_batch = input_batch.to('cuda')
        model = model.to('cuda')

    with torch.no_grad():
        output = model(input_batch)
    
    # Convert output tensor to PIL Image
    output_image = transforms.ToPILImage()(output.squeeze(0))

    # Save the output image
    output_path = "/home/azureUser/Desktop/upload/segmentation/"  # Set your desired output path
    output_filename = os.path.join(output_path, file.filename)
    output_image.save(output_filename)

    result = "Success"
    
    return {"result": result}

@app.post("/convert")
async def save_convert_dcm_to_png(file: UploadFile = File(...), originalName: str = Form(...)):
    try:
        # org_file_name = file.filename
        # print(org_file_name)
        # Save the uploaded DICOM file temporarily
        temp_dcm_path = f"temp_{file.filename}"
        with open(temp_dcm_path, "wb") as buffer:
            contents = await file.read()
            buffer.write(contents)

        # Read DICOM file
        ds = pydicom.dcmread(temp_dcm_path)

        # Convert DICOM data to a PIL image
        img = ds.pixel_array
        img = img - np.min(img)
        if np.max(img) != 0:
            img = img / np.max(img)
        img = (img * 255).astype(np.uint8)
        img_pil = Image.fromarray(img)
        
        # Define path for the output PNG file
        output_path = "/home/azureUser/Desktop/upload/original/"  # Folder where converted images will be saved
        os.makedirs(output_path, exist_ok=True)

        # Use original file name for output file, replacing .dcm with .png
        original_filename_without_extension = os.path.splitext(originalName)[0]
        output_file_path = os.path.join(output_path, f"{original_filename_without_extension}.png")

        # output_file_path = os.path.join(output_path, file.filename.replace('.dcm', '.png'))
        
        # Save the image as PNG
        img_pil.save(output_file_path)

        # Delete the temporary DICOM file
        os.remove(temp_dcm_path)

        # Return the path of the converted PNG file
        return {"converted_file_path": output_file_path}
    except Exception as e:
        print(f"오류: {e}")
        return {"error": str(e)}
    
def convert_dcm_to_png(contents):
    ds = pydicom.dcmread(BytesIO(contents))
    
    img = ds.pixel_array
    img = img - np.min(img)
    
    if np.max(img) != 0:
        img = img / np.max(img)
    
    img = (img * 255).astype(np.uint8)
    img_pil = Image.fromarray(img)  # Convert numpy array to PIL Image
    
    if img_pil.mode != 'RGB':
        img_pil = img_pil.convert('RGB')  # Convert grayscale to RGB
    
    return img_pil

def prepare_image_for_model(image, expected_size=(224, 224)):
    # Resize image
    if image.size != expected_size:
        image = image.resize(expected_size)

    # Ensure image has three channels (RGB)
    if image.mode != 'RGB':
        image = image.convert('RGB')

    return image