import MockAdapter from 'axios-mock-adapter'
import api from './axios'

const mock = new MockAdapter(api, { delayResponse: 500 })

mock.onGet('/reservas/OK123').reply(200, {
  error: false,
  data: {
    codigo: 'OK123',
    estado: 'CONFIRMADA',
    cliente: {
      nome: 'João da Silva',
    },
    voo: {
      codigo: 'FL123',
      origem: 'GRU',
      destino: 'GIG',
    },
  },
})

mock.onGet('/reservas/EXPIRE').reply(401, {
  error: true,
  statusCode: 401,
  message: 'Token expirado',
})

mock.onGet('/reservas/NADA').reply(404, {
  error: true,
  statusCode: 404,
  message: 'Reserva não encontrada',
})

export default mock
