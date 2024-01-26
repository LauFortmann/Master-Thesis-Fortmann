import os
import sys
from os.path import dirname, join
import numpy as np
import torch
import torch.nn as nn
from transformers import Wav2Vec2Processor
from transformers.models.wav2vec2.modeling_wav2vec2 import (
    Wav2Vec2Model,
    Wav2Vec2PreTrainedModel,

)


def get_model():
    class RegressionHead(nn.Module):
        r"""Classification head."""

        def __init__(self, config):
            super().__init__()

            self.dense = nn.Linear(config.hidden_size, config.hidden_size)
            self.dropout = nn.Dropout(config.final_dropout)
            self.out_proj = nn.Linear(config.hidden_size, config.num_labels)

        def forward(self, features, **kwargs):
            x = features
            x = self.dropout(x)
            x = self.dense(x)
            x = torch.tanh(x)
            x = self.dropout(x)
            x = self.out_proj(x)

            return x

    class EmotionModel(Wav2Vec2PreTrainedModel):
        r"""Speech emotion classifier."""

        def __init__(self, config):
            super().__init__(config)

            self.config = config
            self.wav2vec2 = Wav2Vec2Model(config)
            self.classifier = RegressionHead(config)
            self.init_weights()

        def forward(
                self,
                input_values,
        ):
            outputs = self.wav2vec2(input_values)
            hidden_states = outputs[0]
            hidden_states = torch.mean(hidden_states, dim=1)
            logits = self.classifier(hidden_states)

            return hidden_states, logits

    filename = join(dirname(__file__), 'wav2vec2-large-robust-12-ft-emotion-msp-dim')
    model = EmotionModel.from_pretrained(filename, local_files_only=True)
    return model


def get_processor():
    """loads preprocessor from the files"""
    filename = join(dirname(__file__), 'wav2vec2-large-robust-12-ft-emotion-msp-dim')
    processor = Wav2Vec2Processor.from_pretrained(filename, local_files_only=True)
    return processor


def preprocess_data(processor, data):
    """preprocesses and creates dummy data if necessary"""
    sampling_rate = 16000
    signal = np.zeros((1, sampling_rate), dtype=np.float32)
    device = 'cpu'
    y = processor(data, sampling_rate=sampling_rate)
    y_dummy = processor(signal, sampling_rate= sampling_rate)
    print(y)
    print(y_dummy)

    #y = y['input_values'][0]
    y = y['input_values']
    y = torch.from_numpy(y).to(device)

    y_dummy = y_dummy['input_values'][0]
    y_dummy = torch.from_numpy(y_dummy).to(device)
    print(y)
    print(y_dummy)

    print(type(y.size()))
    print(type(y_dummy.size()))

    print(y)
    print(y_dummy)
    return y


def label_data(preprocessed_data, model):
    """labels preprocessed data"""
    with torch.no_grad():
        preprocessed_data = preprocessed_data.to(torch.float32)
        y = model(preprocessed_data)[1]
    # convert to numpy
    y = y.detach().cpu().numpy()
    return y.tolist()

def read_file(path_to_voice_file):
    """read file from given path and converts it to a numpy array"""
    with open(path_to_voice_file) as f:
        voice_rec = np.fromfile(f)
    return voice_rec
