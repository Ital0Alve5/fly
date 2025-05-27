import type { Address } from '@/types/Api'
import type { Client } from '@/types/Auth/AuthenticatedUserData'
function limparNumero(valor: string): string {
    return valor.replace(/\D/g, '')
  }
  
function formatarEndereco(endereco: Address): Address {
    return {
        cep: limparNumero(endereco?.cep ?? ''),
        uf: endereco?.uf ?? '',
        cidade: endereco?.cidade ?? '',
        bairro: endereco?.bairro ?? '',
        rua: endereco?.rua ?? '',
        numero: endereco?.numero.toString() ?? '',
        complemento: endereco?.complemento ?? ''
    }
}

export function formatarUsuario(usuario: Client): Client {
    return {
        codigo: usuario?.codigo ?? -1,
        cpf: limparNumero(usuario?.cpf ?? ''),
        email: usuario?.email ?? '',
        nome: usuario?.nome ?? '',
        saldo_milhas: usuario?.saldo_milhas ?? 0,
        endereco: formatarEndereco(usuario?.endereco)
    }
}
