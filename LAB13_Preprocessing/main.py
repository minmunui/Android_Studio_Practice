from PIL import Image
from tqdm import tqdm
# import tensorflow as tf
import binascii
import glob
import os

import numpy as np

dataCnt = 0
dataNum = 9000
width = 100
height = 100
channels = 3
dataset = np.zeros(shape=(dataNum, height, width, channels), dtype=np.float32)


def main():
    global dataCnt
    malware_path = './bytes'
    malware_categories = os.listdir(malware_path + '/')
    print(malware_categories)

    dataCnt = 0
    cnt = 0

    for malware_family in tqdm(malware_categories):
        print(malware_family)
        malware_files = glob.glob(malware_path + '/' + malware_family + '/*.txt')
        for malware_file in tqdm(malware_files):
            if not malware_file:
                continue
            try:
                f = open(malware_file, 'r')
                #                 print("FILE OPEN")
                code = f.readlines()
                one_line = ''
                for line in code:
                    line = line.replace("+", "")
                    line = line.replace("\n", "")

                    if len(line) != 2:
                        print(str(len(line)) + "### " + line)
                    one_line = one_line + line

                BinarySrcCode = binascii.unhexlify(one_line)
                code_len = len(BinarySrcCode)
                padLen = width * height * 3 - code_len

                if code_len == 0:
                    continue
                if padLen < 0:
                    print("file is over")
                    cnt += 1
                    continue

                zeroPad = bytes(padLen)
                image = bytearray(BinarySrcCode) + bytes(zeroPad)
                img = Image.frombytes("RGB", (height, width), bytes(list(image)))
                #                 print("READY TO SAVE")
                img.save("./malware_images/" + malware_family + "/" + malware_file[17:-4] + ".PNG", 'PNG')
                dataCnt += 1

            except Exception as ex:
                #                 print("FILE NOT OPENED")
                print(malware_file)
                print(ex)
                return

        print("num of exceeded sol :", cnt)
        print("num of total vul :", dataCnt)

    return


main()

from keras.preprocessing.image import ImageDataGenerator
import tensorflow as tf

data_dir = './malware_images/'
datagen = ImageDataGenerator(rescale=1. / 255, validation_split=0.2)

train_it = datagen.flow_from_directory(
    data_dir,
    class_mode='categorical',
    target_size=(100, 100),
    batch_size=32)

val_it = validation_generator = datagen.flow_from_directory(
    data_dir,
    target_size=(100, 100),
    batch_size=32,
    subset='validation')

cnn_model = tf.keras.models.Sequential([
    tf.keras.layers.Conv2D(32, (3, 3), activation='relu', input_shape=(100, 100, 1)),
    tf.keras.layers.MaxPooling2D((2, 2)),
    tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
    tf.keras.layers.MaxPooling2D((2, 2)),
    tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dense(64, activation='relu'),
    tf.keras.layers.Dense(9, activation='softmax')
])

cnn_model.compile(optimizer='adam',
                  loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])

cnn_model.fit(train_it, epochs=10)
test_eval_result = cnn_model.evaluate(val_it)
print(test_eval_result)

converter = tf.lite.TFLiteConverter.from_keras_model(cnn_model)
tflite_model = converter.convert()

with open('./keras_model_cnn_malware.tflite', 'wb') as f:
    f.write(tflite_model)
