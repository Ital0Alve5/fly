import * as express from 'express'

const app = express()

app.use(express.json())

app.get('/', (_, res) => {
    res.send({hello: 'world'})
})

app.listen(8000, () => {
   console.log("API gateway started at port 8000")
})
