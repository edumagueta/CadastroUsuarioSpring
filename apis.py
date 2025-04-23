from threading import Thread
from flask import Flask, request
from random import randint


## ---------------------------------------------------
## Federal Manager
app_federal = Flask(__name__)

@app_federal.route("/federal-manager/<name>")
def federal_information(name: str):
    return {
        'nome_pessoa': name,
        'numero_cpf': __rand_cpf(),
        'telefone': f'15{__rand_with_size(9)}',
        'situacao_cadastral': 'REGULAR',
        'endereco': {
            'tipo_logradouro': 'Rua',
            'logradouro': f'Rua {__rand_with_size(3)}',
            'numero': 'S/N',
            'complemento': 'N/A',
            'municipio': 'Sorocaba',
            'uf': 'São Paulo'
        }
    }

def __rand_cpf() -> str:
    return f"{__rand_with_size(3)}.{__rand_with_size(3)}.{__rand_with_size(3)}-{__rand_with_size(2)}"

def __rand_with_size(size: int) -> str:
    return ''.join([str(randint(0, 9)) for i in range(size)])

def run_app_federal():
    app_federal.run(port=9000)


## ---------------------------------------------------
## Credit Analysis
app_credit = Flask(__name__)

@app_credit.route("/credit-analysis/person")
def get_credit_analysis():
    document = request.args.get('document')
    score = randint(0, 1000)
    return {
        'document': document,
        'creditScore': score,
        'rating': 'A' if score > 700 else 'B' 
    }

def run_app_credit():
    app_credit.run(port=9001)


## ---------------------------------------------------
## ---------------------------------------------------

if __name__ == "__main__":
    t1 = Thread(target=run_app_federal)
    t2 = Thread(target=run_app_credit)

    t1.start()
    t2.start()

    t1.join()
    t2.join()