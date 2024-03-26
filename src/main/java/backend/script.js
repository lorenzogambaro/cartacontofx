import express, { response } from 'express'
import mysql from 'mysql2'
import cors from 'cors'
import { generateIban } from './bank_functions.js'

const app = express()

const pool = mysql.createPool({
    host: "localhost",
    user: "root",
    password: "",
    database: "db_banca"
    
}).promise()

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors())

app.get('/cartaconto/intestatari/', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM intestatari`)
        
        for (let intestatario of response)
        {
            const [id_conti_associati] = await pool.query(`SELECT iban_conto FROM intestatari_conti WHERE id_intestatario = ?`, [intestatario.id_intestatario])
    
            const [power] = await pool.query(`SELECT * FROM potenzeIntestatario WHERE id_potenza = ? LIMIT 1`, intestatario.power)
    
            intestatario.iban_conti_associati = id_conti_associati.map((value) => value.iban_conto) 
            intestatario.power = power[0]   
            
        }

   
        res.send({ status: 200, result: response })

    }
    catch (exception)
    {
        console.error(exception)
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/getfirstavailableiban', async (req, res) => 
{
    try
    {
        const [result] = await pool.query(`SELECT COUNT(*) AS numero_righe FROM conti`)
        const [banca] = await pool.query(`SELECT * FROM banche LIMIT 1`)

        const iban = generateIban(banca[0].location_chars, banca[0].abi_code, banca[0].cab_code, result[0].numero_righe.toString())
        
        res.send({ status: 200, result : iban})
    }
    catch (exception) 
    {
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/tipimovimento', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM tipimovimento`)
       
        const data = response.map((element) => 
        {
            element.direction = element.direction.readInt8() !== 0
            return element
        })
        
        res.send({ status: 200, result: data })

    }
    catch (exception)
    {
        res.send({ status: 400 })
    }

})

app.get('/cartaconto/banche/:abi', async (req, res) => {
    
    try
    {
        const [response] = await pool.query(`SELECT * FROM banche WHERE abi_code = ?`, [req.params.abi])
        res.send({ status: 200, result : response })
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
    
})

app.get('/cartaconto/conti', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM conti`)
        
        for (let conto of response)
        {
            const [id_intestatari] = await pool.query(`SELECT id_intestatario FROM intestatari_conti WHERE iban_conto = ?`, conto.iban_conto)
    
            const usernames = []
    
            for (const obj_id of id_intestatari)
            {
                const [val] = await pool.query(`SELECT username FROM intestatari WHERE id_intestatario = ? LIMIT 1`, obj_id.id_intestatario)
                usernames.push(val[0].username)
            }
    
            const [movimenti_cui_destinatario] = await pool.query('SELECT * FROM movimenti WHERE iban_destinatario = ?', conto.iban_conto)
    
    
            for (const movement of movimenti_cui_destinatario)
            {
                const [type_movement] = (await pool.query('SELECT * FROM tipimovimento WHERE id_type = ? LIMIT 1', movement.movement_type))[0]
                type_movement.direction = type_movement.direction.readInt8() !== 0
                movement.movement_type = type_movement
            }
    
            const [movimenti_cui_richiedente] = await pool.query('SELECT * FROM movimenti WHERE iban_richiedente = ?', conto.iban_conto)
    
            
            for (const movement of movimenti_cui_richiedente)
            {
                const [type_movement] = (await pool.query('SELECT * FROM tipimovimento WHERE id_type = ? LIMIT 1', movement.movement_type))[0]
                type_movement.direction = type_movement.direction.readInt8() !== 0
                movement.movement_type = type_movement
            }
            
            conto.intestatari_usernames = usernames
            conto.movimenti_cui_destinatario = movimenti_cui_destinatario
            conto.movimenti_cui_richiedente = movimenti_cui_richiedente

        }


        res.send({ status: 200, result : response })
    }
    catch (exception)
    {
        console.log(exception)
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/potenzeintestatario', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM potenzeIntestatario`)
        
        res.send({ status: 200, result: response })

    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/conti/:iban', async (req, res) => 
{
    try
    {
        const [response] = (await pool.query(`SELECT * FROM conti WHERE iban_conto = ? LIMIT 1`, req.params.iban))[0]
        
        const [id_intestatari] = await pool.query(`SELECT id_intestatario FROM intestatari_conti WHERE iban_conto = ?`, req.params.iban)

        const usernames = []

        for (const obj_id of id_intestatari)
        {
            const [val] = await pool.query(`SELECT username FROM intestatari WHERE id_intestatario = ? LIMIT 1`, obj_id.id_intestatario)
            usernames.push(val[0].username)
        }

        const [movimenti_cui_destinatario] = await pool.query('SELECT * FROM movimenti WHERE iban_destinatario = ?', req.params.iban)

        
        for (const movement of movimenti_cui_destinatario)
        {
            const [type_movement] = (await pool.query('SELECT * FROM tipimovimento WHERE id_type = ? LIMIT 1', movement.movement_type))[0]
            type_movement.direction = type_movement.direction.readInt8() !== 0
            movement.movement_type = type_movement
        }
        
        const [movimenti_cui_richiedente] = await pool.query('SELECT * FROM movimenti WHERE iban_richiedente = ?', req.params.iban)


        for (const movement of movimenti_cui_richiedente)
        {
            const [type_movement] = (await pool.query('SELECT * FROM tipimovimento WHERE id_type = ? LIMIT 1', movement.movement_type))[0]
            type_movement.direction = type_movement.direction.readInt8() !== 0
            movement.movement_type = type_movement
        }

        response.intestatari_usernames = usernames
        response.movimenti_cui_destinatario = movimenti_cui_destinatario
        response.movimenti_cui_richiedente = movimenti_cui_richiedente

        res.send({ status: 200, result : response })
    }
    catch (exception)
    {
        console.log(exception)
        res.send({ status: 400 })
    }
})

app.get('/cartaconto/intestatari/:username', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM intestatari WHERE username = ? LIMIT 1`, [req.params.username])
        
        const [id_conti_associati] = await pool.query(`SELECT iban_conto FROM intestatari_conti WHERE id_intestatario = ?`, [response[0].id_intestatario])

        const [power] = await pool.query(`SELECT * FROM potenzeIntestatario WHERE id_potenza = ? LIMIT 1`, response[0].power)
        
        response[0].iban_conti_associati = id_conti_associati.map((value) => value.iban_conto) 
        response[0].power = power[0]
   
        res.send({ status: 200, result: response[0] })
        
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/administrators', async (req, res) => 
{
    try
    {
        const [result] = await pool.query('SELECT * FROM administrators')

        for (let admin of result)
        admin.power_level = (await pool.query('SELECT * FROM adminpowers WHERE id_power = ? LIMIT 1', admin.power_level))[0][0]

    
    res.send({ status: 200, result: result })
}
catch (exception)
    {
        res.send({ status: 400 })
    }
})
app.get('/cartaconto/movimenti/:id', async (req, res) =>
{
    try
    {
        const [response] = await pool.query('SELECT * FROM movimenti WHERE id_movimento = ? LIMIT 1', req.params.id)

        res.send({ status: 200, result: response })
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
    

})
app.get('/cartaconto/potenzeintestatario/:id', async (req, res) => 
{
    try
    {
        const [response] = await pool.query(`SELECT * FROM potenzeintestatario WHERE id_potenza = ? LIMIT 1`, req.params.id)
        
        res.send({ status: 200, result: response[0] })
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
})

app.get('/cartaconto/tipimovimento/:id', async (req, res) => 
{
    try 
    {
        const [response] = await pool.query(`SELECT * FROM tipimovimento WHERE id_type = ? LIMIT 1`, req.params.id)
        
        response[0].direction = response[0].direction.readInt8() !== 0

        res.send({ status: 200, result: response[0] })
    } 
    catch (exception)
    {
        console.log(exception)
        res.send({ status: 400 })    
    }
})
app.post('/cartaconto/movimenti', async (req, res) => 
{
    try
    {
        const [result] = await pool.query(`INSERT INTO movimenti (iban_richiedente, iban_destinatario, operation_date, valute_date, causal, amount, movement_type) VALUES (?, ?, ?, ?, ?, ?, ?)`,
        [req.body.ibanRichiedente, req.body.ibanDestinatario, req.body.operationDate, req.body.valuteDate, req.body.descr, req.body.importo, req.body.type]
        )
        
        res.send({ status: 200, response: result})
        
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }

})
app.post('/cartaconto/intestatari', async (req, res) => {
    try
    {
        const [result] = await pool.query(`INSERT INTO intestatari (username, hashed_pw, fiscal_code, surname_intestatario, name_intestatario, birth_date, via, numero, cap, comune, provincia, phoneNumber, email_address, power) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`,
        [req.body.username, req.body.hashed_pw, req.body.fiscal_code, req.body.surname_intestatario, req.body.name_intestatario, req.body.birth_date, req.body.via, req.body.numero, req.body.cap, req.body.comune, req.body.provincia, req.body.phoneNumber, req.body.email_address, req.body.power]  
        )
        
        res.send({ status: 200, response: result})
    }
    catch (exception)
    {
        res.send({ status: 400 })
        console.error(exception)
    }
})
app.get('/cartaconto/administrators/:username', async (req, res) => 
{
    try
    {
        const [result] = await pool.query('SELECT * FROM administrators WHERE username = ? LIMIT 1', req.params.username)

        result[0].power_level = (await pool.query('SELECT * FROM adminpowers WHERE id_power = ?', result[0].power_level))[0][0]

        res.send({ status: 200, result: result[0] })
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
})

app.post('/cartaconto/intestatari/:fiscal_code', async (req, res) => 
{
    let query = `UPDATE intestatari SET `
    
    const values = []
    
    Object.keys(req.body).forEach(async (key, i) => 
    {
        query += `${i !== 0 ? ', ' : ""}${key} = ?`
        values.push(req.body[key])
    })
    
    query += ` WHERE fiscal_code = ?`
    values.push(req.params.fiscal_code)

    try
    {
        const [response] = await pool.query(query, values)
        
        res.send({ status: 200, result : response })
    }
    catch (exception)
    {
        res.send({ status: 400 })
    }
    
    
})
app.post('/cartaconto/conti', async (req, res) => 
{ 
    console.log(req.body)
    try
    {
        const [result] = await pool.query(`SELECT COUNT(*) AS numero_righe FROM conti`)
        const [banca] = await pool.query(`SELECT * FROM banche LIMIT 1`)
        const iban = generateIban(banca[0].location_chars, banca[0].abi_code, banca[0].cab_code, result[0].numero_righe.toString())
        
        console.log(iban, req.body.iban)
        if (iban !== req.body.iban)
            throw 'IbanMismatch'
    
        let [responseConto] = await pool.query(`INSERT INTO conti (iban_conto, opening_date, abi_banca_associata, cab_banca_associata) VALUES (?, NOW(), ?, ?)`, [iban, banca[0].abi_code, banca[0].cab_code])
        
        req.body.intestatari.forEach(async (intestatario) => 
        {
            const [intestatarioCode] = await pool.query(`SELECT id_intestatario FROM intestatari WHERE fiscal_code = ?`, intestatario)
            const [response] = await pool.query(`INSERT INTO intestatari_conti (id_intestatario, iban_conto) VALUES (?, ?)`, [intestatarioCode[0].id_intestatario, iban])

            responseConto = {...responseConto, ...response}
        })

        res.send({ status: 200, result : responseConto })
    }
    catch (exception)
    {
        console.error(exception)
        res.send({ status: 400 })
    }
    
})


app.listen(8211, () => console.log("Listening to port 8211"))