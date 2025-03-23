<script setup lang="ts">
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '../../ui/card'
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from '../../ui/form'
import { Input } from '../../ui/input'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '../../ui/tabs'
import { Button } from '../../ui/button'
</script>

<template>
  <Tabs default-value="login" class="w-[400px]">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="register">
        Cadastro
      </TabsTrigger>
      <TabsTrigger value="login">
        Login
      </TabsTrigger>
    </TabsList>
    <TabsContent value="register">
      <Card>
        <CardHeader>
          <CardTitle>Cadastro</CardTitle>
          <CardDescription>
            Crie sua conta para ter acesso a todos os recursos.
          </CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit="onSubmit">
            <FormField name="name">
              <FormItem>
                <FormLabel>Nome</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="Nome"
                    v-bind="componentField"
                    @input="handleNameInput"
                    maxlength="60"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="email">
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="Email"
                    v-bind="componentField"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="cpf">
              <FormItem>
                <FormLabel>CPF</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="000.000.000-00"
                    v-bind="componentField"
                    @input="handleCpfInput"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="cep">
              <FormItem>
                <FormLabel>CEP</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="00000-000"
                    v-bind="componentField"
                    @input="handleCepInput"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="rua">
              <FormItem>
                <FormLabel>Rua</FormLabel>
                <FormControl>
                  <Input
                    type="text"
                    placeholder="Rua"
                    v-bind="componentField"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <div class="grid grid-cols-2 gap-4">
              <FormField name="numero">
                <FormItem>
                  <FormLabel>Número</FormLabel>
                  <FormControl>
                    <Input
                      type="text"
                      placeholder="Número"
                      v-bind="componentField"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField name="complemento">
                <FormItem>
                  <FormLabel>Complemento</FormLabel>
                  <FormControl>
                    <Input
                      type="text"
                      placeholder="Complemento"
                      v-bind="componentField"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <FormField name="cidade">
                <FormItem>
                  <FormLabel>Cidade</FormLabel>
                  <FormControl>
                    <Input
                      type="text"
                      placeholder="Cidade"
                      v-bind="componentField"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
              <FormField name="estado">
                <FormItem>
                  <FormLabel>Estado</FormLabel>
                  <FormControl>
                    <Input
                      type="text"
                      placeholder="Estado"
                      v-bind="componentField"
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              </FormField>
            </div>
            <CardFooter class="p-0 pt-4">
              <Button type="submit" class="w-full">Cadastrar</Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>
    <TabsContent value="login">
      <Card>
        <CardHeader>
          <CardTitle>Login</CardTitle>
          <CardDescription>
            Entre com sua conta para ter acesso a todos os recursos.
          </CardDescription>
        </CardHeader>
        <CardContent class="space-y-2">
          <form @submit="onSubmit">
            <FormField name="email">
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="Email"
                    v-bind="componentField"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <FormField name="password">
              <FormItem>
                <FormLabel>Senha</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="Senha"
                    v-bind="componentField"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            </FormField>
            <CardFooter class="p-0 pt-4">
              <Button type="submit" class="w-full">Login</Button>
            </CardFooter>
          </form>
        </CardContent>
      </Card>
    </TabsContent>
  </Tabs>
</template>