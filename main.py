from fastapi import FastAPI, File, UploadFile
import torch
import numpy as np
from PIL import Image
from torchvision import transforms
from io import BytesIO
import os

app = FastAPI()

@app.post("/predict")
async def predict(file: UploadFile = File(...)):

    # Load model
    model = torch.hub.load('mateuszbuda/brain-segmentation-pytorch', 'unet',
    in_channels=3, out_channels=1, init_features=32, pretrained=True)

    # Read file
    contents = await file.read()
    image = Image.open(BytesIO(contents))

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
    output_path = "/Users/bagjuhyeog/Desktop/curexo/upload/segmentation/"  # Set your desired output path
    output_filename = os.path.join(output_path, file.filename)
    output_image.save(output_filename)

    result = "Success"
    
    return {"result": result}